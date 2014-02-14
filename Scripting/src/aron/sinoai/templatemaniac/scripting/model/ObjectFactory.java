package aron.sinoai.templatemaniac.scripting.model;

import javax.xml.bind.annotation.XmlRegistry;

import aron.sinoai.templatemaniac.scripting.model.matcher.AnyChar;
import aron.sinoai.templatemaniac.scripting.model.matcher.CharInString;
import aron.sinoai.templatemaniac.scripting.model.matcher.CharNotInString;
import aron.sinoai.templatemaniac.scripting.model.matcher.Empty;
import aron.sinoai.templatemaniac.scripting.model.matcher.EndOfParse;
import aron.sinoai.templatemaniac.scripting.model.matcher.FixNrOfTimes;
import aron.sinoai.templatemaniac.scripting.model.matcher.MatchOneAfterAnother;
import aron.sinoai.templatemaniac.scripting.model.matcher.MatchOneOfThem;
import aron.sinoai.templatemaniac.scripting.model.matcher.MatcherContainer;
import aron.sinoai.templatemaniac.scripting.model.matcher.DynamicMatcher;
import aron.sinoai.templatemaniac.scripting.model.matcher.MatcherReference;
import aron.sinoai.templatemaniac.scripting.model.matcher.NoDigit;
import aron.sinoai.templatemaniac.scripting.model.matcher.NoWhite;
import aron.sinoai.templatemaniac.scripting.model.matcher.NullOrManyMulti;
import aron.sinoai.templatemaniac.scripting.model.matcher.OneDigit;
import aron.sinoai.templatemaniac.scripting.model.matcher.OneOrManyMulti;
import aron.sinoai.templatemaniac.scripting.model.matcher.OneWhite;
import aron.sinoai.templatemaniac.scripting.model.matcher.RegExp;
import aron.sinoai.templatemaniac.scripting.model.matcher.StringMatch;
import aron.sinoai.templatemaniac.scripting.model.matcher.StringPatternMatch;
import aron.sinoai.templatemaniac.scripting.model.matcher.UntilTheString;

@XmlRegistry 
public class ObjectFactory {
	
   ScriptContainer createScriptContainer() {
	   return new ScriptContainer();
   }

   GeneratorContainer createGeneratorConatiner() {
	   return new GeneratorContainer();
   }

   FileGenerator createFileGenerator() {
	   return new FileGenerator();
   }
   
   HqlDataSource createHqlDataSource() {
	   return new HqlDataSource();
   }

   SqlDataSource createSqlDataSource() {
	   return new SqlDataSource();
   }

   DynamicContent createDynamicContent() {
	   return new DynamicContent();
   }
   
   ExternalScript createExternalScript() {
	   return new ExternalScript();
   }
   
   IncludeScript createIncludeScript() {
	   return new IncludeScript();
   }
   
   IncludeDynamicScript createDynamicIncludeScript() {
	   return new IncludeDynamicScript();
   }
   
   Message createMessage() {
	   return new Message();
   }
   
   ListGenerator createGenerator() {
	   return new ListGenerator();
   }
   
   MapGenerator createMapGenerator() {
	   return new MapGenerator();
   }
   
   ResultTansformer createResultTansformer() {
	   return new ResultTansformer();
   }
   
   Formatter createFormatter() {
	   return new Formatter();
   }
   
   ScriptingTemplate createScriptingTemplate() {
	   return new ScriptingTemplate();
   }
   
   AnyChar createAnyChar() {
	   return new AnyChar();
   }
   
   MatcherContainer createMatcherContainer() {
	   return new MatcherContainer();
   }
   
   MatcherReference createMatcherReference() {
	   return new MatcherReference();
   }
 
   DynamicMatcher createDynamicMatcher() {
	   return new DynamicMatcher();
   }
   
   CharInString createCharInString() {
	   return new CharInString();
   }
   
   CharNotInString createCharNotInString() {
	   return new CharNotInString();
   }
   
   Empty createEmpty() {
	   return new Empty();
   }
   
   EndOfParse createEndOfParse() {
	   return new EndOfParse();
   }

   FixNrOfTimes createFixNrOfTimes() {
	   return new FixNrOfTimes();
   }
   
   MatchOneOfThem createMatchOneOfThem() {
	   return new MatchOneOfThem();
   }
   
   NoDigit createNoDigit() {
	   return new NoDigit();
   }

   NoWhite createNoWhite() {
	   return new NoWhite();
   }

   NullOrManyMulti createNullOrManyMulti() {
	   return new NullOrManyMulti();
   }

   MatchOneAfterAnother createMatchOneAfterAnother() {
	   return new MatchOneAfterAnother();
   }

   OneDigit createOneDigit() {
	   return new OneDigit();
   }

   OneOrManyMulti createOneOrManyMulti() {
	   return new OneOrManyMulti();
   }
   
   OneWhite createOneWhite() {
	   return new OneWhite();
   }
   
   StringMatch createStringMatch() {
	   return new StringMatch();
   }

   RegExp createRegExp() {
	   return new RegExp();
   }
   
   UntilTheString createUntilTheString() {
	   return new UntilTheString();
   }

   FileDataSource createFileDataSource() {
	   return new FileDataSource();
   }
   
   StringPatternMatch createStringPatternMatch()  {
	   return new StringPatternMatch();
   }
   
   DynamicExtension createDynamicExtension() {
	   return new DynamicExtension();
   }
}
