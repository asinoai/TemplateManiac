package aron.sinoai.templatemaniac.scripting.model;

import org.antlr.stringtemplate.AttributeRenderer;

import aron.sinoai.templatemaniac.scripting.util.FormatHelper;
import aron.sinoai.templatemaniac.scripting.util.FormatHelper.FormatterKind;

public class BasicFormatRenderer implements AttributeRenderer {

	@Override
	public String toString(Object o) {
		return o.toString();
	}

	@Override
	public String toString(Object o, String formatName) {
		if (formatName.equals("toUpper")) {
            return FormatHelper.format(FormatterKind.UpperCase, o.toString());
        } else if (formatName.equals("toLower")) {
            return FormatHelper.format(FormatterKind.LowerCase, o.toString());
        } else if (formatName.equals("toUpperCammel")) {
            return FormatHelper.format(FormatterKind.UpperCammelCase, o.toString());
        } else if (formatName.equals("toLowerCammel")) {
            return FormatHelper.format(FormatterKind.LowerCammelCase, o.toString());
        } else if (formatName.equals("toSafeFileName")) {
            return FormatHelper.format(FormatterKind.SafeFileName, o.toString());
        } else if (formatName.equals("prettyPrintXML")) {
            return FormatHelper.format(FormatterKind.PrettyPrintXML, o.toString());
        } else if (formatName.equals("escapeXML")) {
            return FormatHelper.format(FormatterKind.EscapeXML, o.toString());
        } else if (formatName.equals("conditional")) {
            return FormatHelper.format(FormatterKind.Conditional, o.toString());
        } else if (formatName.equals("expression2preorder")) {
            return FormatHelper.format(FormatterKind.Expression2Preorder, o.toString());
        } else if (formatName.equals("encodeBase64")) {
            return FormatHelper.format(FormatterKind.EncodeBase64, o.toString());
        } else {
            throw new IllegalArgumentException("Unsupported format name");
        }
	}
}
