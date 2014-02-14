package aron.sinoai.templatemaniac.scripting.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class FileResource implements TargetResource {

	private Writer writer;

	private File temporaryFile;
	private File originalFile;
	
	public FileResource(File file, String encoding, boolean noLock) {
		originalFile = file;
		
		try {
			if (noLock) {
				temporaryFile = File.createTempFile(file.getName(), "tmp", file.getParentFile());
				file = temporaryFile;
			}
			
			if (encoding != null) {
				final OutputStream stream = new FileOutputStream(file);
			
				final OutputStreamWriter outputWriter;
					outputWriter = new OutputStreamWriter(stream, encoding);
			
				writer = new BufferedWriter(outputWriter);
			} else {
				writer = new BufferedWriter(new FileWriter(file));
			}
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}		
	}
	
	@Override
	public void append(String value) {
		try {
			writer.append(value);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void close() {
		if (writer != null) {
			try {
				writer.flush();
				writer.close();
				writer = null;
				
				if (temporaryFile != null) {
					if (originalFile.exists()) {
						originalFile.delete();
					}
					
					temporaryFile.renameTo(originalFile);
					temporaryFile = null;
				}
				
				originalFile = null;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}
	}

}
