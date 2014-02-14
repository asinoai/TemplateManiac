package aron.sinoai.templatemaniac.scripting.model;


import java.io.File;
import java.util.Collection;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.antlr.stringtemplate.StringTemplate;

@XmlRootElement(name = "file-generator")
@XmlAccessorType(XmlAccessType.FIELD)
public class FileGenerator extends GeneratorTargetBase {

	@XmlAttribute
	private String fileNamePattern;
	
	@XmlAttribute
	private String encoding;

	@XmlAttribute
	private boolean noLock = false;

	@XmlAttribute
	private boolean forceWritable = false;

	@XmlAttribute
	private String pageSizePattern;
	
	@XmlAttribute(name = "pagedDs")
	private String pagedDsName;
	
	@XmlElement(name = "on-page-start")
	private GeneratorContainer onPageStart;
	
	@XmlElement(name = "on-page-end")
	private GeneratorContainer onPageEnd;
	
	@XmlTransient
	private TargetResource targetResource;

	@XmlTransient
	private StringTemplate fileNameTemplate;

	@XmlTransient
	private StringTemplate pageSizeTemplate;

	@XmlTransient
	private Integer pageSize;

	@XmlTransient
	private IterableDataSource pagedDs;
	
	@XmlTransient
	private ScriptingContext context;

	public FileGenerator() {
	}

	public void setFileNamePattern(String fileNamePattern) {
		this.fileNamePattern = fileNamePattern;
	}

	public String getFileNamePattern() {
		return fileNamePattern;
	}

	@Override
	public void write(String key, String value) {
		targetResource.append(value);
	}
		
	@Override
	public void write(String key, Collection<?> value) {
		throw new RuntimeException("FileGenerator doesn't support as target maps or lists!");
	}
	
	@Override
	public void write(String key, Map<?,?> value) {
		throw new RuntimeException("FileGenerator doesn't support as target maps or lists!");
	}
		
	@Override
	public void compile(ScriptingContext context, ScriptingItem parent) {
		super.compile(context, parent);

		fileNameTemplate = createStringTemplate(fileNamePattern, context, parent);
		pageSizeTemplate = createStringTemplate(pageSizePattern, context, parent);
		
		if (isPaged()) {
			//fetching the peer paged data-source (either is the one specified in dsname, or the parent one)
			if (pagedDsName != null) {
				DataSource ds = context.getAll().get(pagedDsName);
				if (ds != null) {
					if (ds instanceof IterableDataSource) {
						pagedDs = (IterableDataSource)ds;
					} else {
						throw new RuntimeException(String.format("FileGenerator '%s': the ds '%s' must be an iterable ds (hql or sql ds)!", getName(), pagedDs));
					}
				} else {
					throw new RuntimeException(String.format("FileGenerator '%s': not found ds ('%s')!", getName(), pagedDs));
				}
			} else {
				DataSource ds = getDataSource();
				if (ds instanceof IterableDataSource) {
					pagedDs = (IterableDataSource)ds;
				} else {
					throw new RuntimeException(String.format("FileGenerator '%s': the current ds must be an iterable ds (hql or sql ds)!", getName(), pagedDs));
				}
			}
		}
		
		this.context = context;
		
		if (onPageStart != null) {
			onPageStart.compile(context, this);
		}
		
		if (onPageEnd != null) {
			onPageEnd.compile(context, this);
		}
	}
	
	@Override
	public void execute() {
		try {
			prepareStringTemplate(fileNameTemplate);
			
			if (isPaged()) {
				//we create the target-resource in case paging only on each page start
				//this way for the entire page, the same file is the target, but each page has its own
				
				int currentIndex = pagedDs.getCurrentIndex();
				
				//we recalculate the pageSize on the very first index
				//usually this will be at the beginning, but in case of nested items, this will be called many time
				if (currentIndex == 0) {
					if (pageSizeTemplate != null) {
						prepareStringTemplate(pageSizeTemplate);
						pageSize = Integer.parseInt(pageSizeTemplate.toString());
					}
				}
				
				if (currentIndex % pageSize == 0) {
					
					if (onPageEnd != null && currentIndex != 0) {
						onPageEnd.execute();
					}
					
					//initializing the special attribute, which exists in this case: currentPage
					int currentPage = currentIndex / pageSize;
					fileNameTemplate.setAttribute("currentPage", currentPage);

					//we close the last page's targetResource, since it will not be used anymore 
					//(since we are starting a new resource for this new page)
					if (targetResource != null) {
						targetResource.close();
					}
					
					createTargetResource();
					
					//the last page's target resource, needs to be closed at the end
					//(note: the target resource can be closed manually too, the autoclose mechanism will skip it in this case)
					context.autocloseTargetResourceAtTheEnd(targetResource);

					if (onPageStart != null) {
						onPageStart.execute();
					}
				} 
			} else {
				createTargetResource();
			}
			
			super.execute();
		} finally {
			if (isPaged()) {
				//we don't close the resource each time at paging
				if (pagedDs.isAtEnd() && onPageEnd != null) {
					onPageEnd.execute();

					if (targetResource != null) {
						targetResource.close();
					}
				}
			} else {
				targetResource.close();
			}
		}
	}

	private void createTargetResource() {
		final File file;
		file = new File(context.formatFileName(fileNameTemplate.toString()));
		//making sure the target folder exists
		file.getParentFile().mkdirs();
		
		if (forceWritable && !file.canWrite()) {
			file.setWritable(true);
		}

		targetResource = context.getFileResource(file, encoding, noLock);
	}

	boolean isPaged() {
		return pageSizePattern != null;
	}
	
	@Override
	public void visitChildrenFromTopToDown(ScriptingItemVisitor visitor) {
		super.visitChildrenFromTopToDown(visitor);
		
		final GeneratorContainer[] onHandlers = {onPageStart, onPageEnd};
		for (GeneratorContainer item : onHandlers) {
			if (item != null) {
				item.visitAllFromTopToDown(visitor);
			}
		}
	}

	public void setPageSizePattern(String pageSizePattern) {
		this.pageSizePattern = pageSizePattern;
	}

	public String getPageSizePattern() {
		return pageSizePattern;
	}

	public void setPagedDsName(String pagedDsName) {
		this.pagedDsName = pagedDsName;
	}

	public String getPagedDsName() {
		return pagedDsName;
	}

	public void setOnPageStart(GeneratorContainer onPageStart) {
		this.onPageStart = onPageStart;
	}

	public GeneratorContainer getOnPageStart() {
		return onPageStart;
	}

	public void setOnPageEnd(GeneratorContainer onPageEnd) {
		this.onPageEnd = onPageEnd;
	}

	public GeneratorContainer getOnPageEnd() {
		return onPageEnd;
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		context = null;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setNoLock(boolean noLock) {
		this.noLock = noLock;
	}

	public boolean isNoLock() {
		return noLock;
	}

	public void setForceWritable(boolean forceWritable) {
		this.forceWritable = forceWritable;
	}

	public boolean isForceWritable() {
		return forceWritable;
	}
	
}
