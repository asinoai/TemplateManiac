package aron.sinoai.templatemaniac.scripting.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.apache.xerces.impl.dv.util.Base64;

public abstract class FormatHelper {
	
	public static enum FormatterKind {
		UpperCase
		, LowerCase
		, UpperCammelCase
		, LowerCammelCase
		, SafeFileName
		, PrettyPrintXML
		, EscapeXML
		, Conditional
		, Expression2Preorder
		, EncodeBase64
	}

	public static String format(FormatterKind kind, String value) {
		String result = value;

		if (kind != null) {
			switch (kind) {
			case LowerCase: {
				result = toLowerCase(value);
				break;
			}
			case UpperCase: {
				result = toUpperCase(value);
				break;
			}
			case LowerCammelCase: {
				result = toLowerCammelCase(value);
				break;
			}
			case UpperCammelCase: {
				result = toUpperCammelCase(value);
				break;
			}
			case SafeFileName: {
				result = toSafeFileName(value);
				break;
			}
			case PrettyPrintXML: {
				result = value;
				break;
			}
			case EscapeXML: {
				result = escapeXML(value);
				break;
			}
			case Conditional: {
				result = conditional(value);
				break;
			}
			case Expression2Preorder: {
				result = expression2Preorder(value);
				break;
			}
			case EncodeBase64: {
				result = encodeBase64(value);
				break;
			}
			default: {
				throw new RuntimeException(String.format(
						"Not supported formatter kind: %s!", kind.toString()));
			}
			}
		}

		return result;
	}

	private static String encodeBase64(String value) {
		return Base64.encode(value.getBytes());
	}

	public static String toLowerCase(String name) {
		return (name.length() > 0) ? name.toLowerCase() : "";
	}

	public static String toUpperCase(String name) {
		return (name.length() > 0) ? name.toUpperCase() : "";
	}

	public static String toLowerCammelCase(String name) {
		return (name.length() > 0) ? name.substring(0, 1).toLowerCase()
				.concat(name.substring(1)) : "";
	}

	public static String toUpperCammelCase(String name) {
		return (name.length() > 0) ? name.substring(0, 1).toUpperCase()
				.concat(name.substring(1)) : "";
	}

	public static String toSafeFileName(String value) {
		StringBuilder result = new StringBuilder();

		for (int i = 0; i < value.length(); ++i) {
			char character = value.charAt(i);
			int asciiCodeOfACharacter = (int) character;
			if ((asciiCodeOfACharacter >= 33 && asciiCodeOfACharacter <= 47)
					|| (asciiCodeOfACharacter >= 58 && asciiCodeOfACharacter <= 64)
					|| (asciiCodeOfACharacter >= 91 && asciiCodeOfACharacter <= 96)
					|| (asciiCodeOfACharacter >= 123 && asciiCodeOfACharacter <= 127)) {

				result = result.append(asciiCodeOfACharacter);
			} else {
				result = result.append(character);
			}
		}

		return result.toString();
	}
	
	private static String escapeXML(String value) {
		value = value.replaceAll("&", "&amp;");
		value = value.replaceAll("<", "&lt;");
		value = value.replaceAll(">", "&gt;");
		value = value.replaceAll("\"", "&quot;");
		value = value.replaceAll("\n", "&#10;");
		value = value.replaceAll("\r", "&#13;");
		return value;
	}
	
	private static String conditional(String data) {
		String[] items = data.split(",", -1);
		boolean condition = true;
		for (int i = 0; condition && (i < items.length); i++) {
			condition = condition && !items[i].isEmpty();
		}
		return (condition) ? "true" : "false";
	}

	
	private static final String OPEN_BRACKET = "open-bracket";
	private static final String CLOSE_BRACKET = "close-bracket";

	private static final Map<String, Integer> OPERATOR_PRECEDENCE;
	private static final Map<String, Integer> OPERATOR_NUMBER_OF_OPERANDS;
	
	private static final Set<String> UNARY_SET;
	
	
	static {
		OPERATOR_PRECEDENCE = new HashMap<String, Integer>();
		OPERATOR_PRECEDENCE.put(OPEN_BRACKET, 0);
		OPERATOR_PRECEDENCE.put(CLOSE_BRACKET, 0);
		OPERATOR_PRECEDENCE.put("or", 3);
		OPERATOR_PRECEDENCE.put("and", 4);

		OPERATOR_PRECEDENCE.put("equals", 5);
		OPERATOR_PRECEDENCE.put("not-equals", 5);
		OPERATOR_PRECEDENCE.put("smaller", 5);
		OPERATOR_PRECEDENCE.put("greater", 5);
		OPERATOR_PRECEDENCE.put("smaller-or-equal", 5);
		OPERATOR_PRECEDENCE.put("greater-or-equal", 5);

		OPERATOR_PRECEDENCE.put("unary-equals", 5);
		OPERATOR_PRECEDENCE.put("unary-not-equals", 5);
		OPERATOR_PRECEDENCE.put("unary-smaller", 5);
		OPERATOR_PRECEDENCE.put("unary-greater", 5);
		OPERATOR_PRECEDENCE.put("unary-smaller-or-equal", 5);
		OPERATOR_PRECEDENCE.put("unary-greater-or-equal", 5);
		
		OPERATOR_PRECEDENCE.put("add", 10);
		OPERATOR_PRECEDENCE.put("sub", 10);
		OPERATOR_PRECEDENCE.put("unary-sub", 10);
		OPERATOR_PRECEDENCE.put("mul", 20);
		OPERATOR_PRECEDENCE.put("div", 20);
		OPERATOR_PRECEDENCE.put("pow", 30);
		
		OPERATOR_NUMBER_OF_OPERANDS = new HashMap<String, Integer>();
		OPERATOR_NUMBER_OF_OPERANDS.put("or", 2);
		OPERATOR_NUMBER_OF_OPERANDS.put("and", 2);
		OPERATOR_NUMBER_OF_OPERANDS.put("equals", 2);
		OPERATOR_NUMBER_OF_OPERANDS.put("not-equals", 2);
		OPERATOR_NUMBER_OF_OPERANDS.put("add", 2);
		OPERATOR_NUMBER_OF_OPERANDS.put("sub", 2);
		OPERATOR_NUMBER_OF_OPERANDS.put("mul", 2);
		OPERATOR_NUMBER_OF_OPERANDS.put("div", 2);
		OPERATOR_NUMBER_OF_OPERANDS.put("pow", 2);
		
		OPERATOR_NUMBER_OF_OPERANDS.put("equals", 2);
		OPERATOR_NUMBER_OF_OPERANDS.put("not-equals", 2);
		OPERATOR_NUMBER_OF_OPERANDS.put("smaller", 2);
		OPERATOR_NUMBER_OF_OPERANDS.put("greater", 2);
		OPERATOR_NUMBER_OF_OPERANDS.put("smaller-or-equal", 2);
		OPERATOR_NUMBER_OF_OPERANDS.put("greater-or-equal", 2);

		OPERATOR_NUMBER_OF_OPERANDS.put("unary-sub", 1);
		OPERATOR_NUMBER_OF_OPERANDS.put("unary-equals", 1);
		OPERATOR_NUMBER_OF_OPERANDS.put("unary-not-equals", 1);
		OPERATOR_NUMBER_OF_OPERANDS.put("unary-smaller", 1);
		OPERATOR_NUMBER_OF_OPERANDS.put("unary-greater", 1);
		OPERATOR_NUMBER_OF_OPERANDS.put("unary-smaller-or-equal", 1);
		OPERATOR_NUMBER_OF_OPERANDS.put("unary-greater-or-equal", 1);
	
		UNARY_SET = new HashSet<String>();
		UNARY_SET.add("sub");
		UNARY_SET.add("equals");
		UNARY_SET.add("not-equals");
		UNARY_SET.add("smaller");
		UNARY_SET.add("greater");
		UNARY_SET.add("smaller-or-equal");
		UNARY_SET.add("greater-or-equal");
	}
	
	public static String expression2Preorder(String o) {
		List<String> polishFormat = new ArrayList<String>();

		Stack<String> stack = new Stack<String>();
		
		try {

			String[] list = o.split("\\<_op_\\>"); //separating the operator and operands
			
			//searching for unary operators and replacing them with the unary counterparts (unary operator <=> no left operand)
			//currently only the sub operator can be also an unary operator
			for(int i = 1; i < list.length; i++) {
				if (OPERATOR_NUMBER_OF_OPERANDS.containsKey(list[i])) {
					if (list[i-1].isEmpty() && i >=2 && !list[i-2].equals(CLOSE_BRACKET)) {
						list[i] = "unary-" + list[i];
					}
				}
			}
			
			for(int i = 0; i < list.length; i++) { 
				String currentItem = list[i];
				if (!currentItem.isEmpty()) {
					if (!OPERATOR_PRECEDENCE.containsKey(currentItem)) {
						//we have an operand here
						polishFormat.add(currentItem);
					} else { //operator
						int precedence = OPERATOR_PRECEDENCE.get(currentItem);
						
						if (precedence > 0) { 
							//not brackets
							while ( !stack.empty() && precedence <= OPERATOR_PRECEDENCE.get(stack.peek()) ) { 
								polishFormat.add(stack.pop());
							}
							stack.push(currentItem);
						} else {
							//brackets
							if (currentItem.equals(CLOSE_BRACKET)) {
								while ( !stack.empty() && (!stack.peek().equals(OPEN_BRACKET) ) ) { 
									polishFormat.add(stack.pop());
								}
								
								if (!stack.empty()) {
									stack.pop();//throwing away the bracket
								}
							} else {
								//open bracket
								stack.push(currentItem);
							}
						}
					}
				}
			}
	
			//we just take what is in the stack
			while ( !stack.empty() ) { 
				polishFormat.add(stack.pop());
			}
			
			
			Stack<String> reverseHelper = new Stack<String>();
			
			//we go and write the result in the "preorder" form
			for (int i = 0; i < polishFormat.size(); i++) {
				String currentItem = polishFormat.get(i);
				if (OPERATOR_PRECEDENCE.containsKey(currentItem)) {
					//operator
					
					StringBuilder piece = new StringBuilder();
					piece.append("<" + currentItem + ">");
					
					int numberOfOperands = OPERATOR_NUMBER_OF_OPERANDS.get(currentItem);
					for (int j = 0; j < numberOfOperands; j++) {
						reverseHelper.push(stack.pop());
					}
					
					while (!reverseHelper.empty()) {
						piece.append(reverseHelper.pop());
					}
	
					piece.append("</" + currentItem + ">");
					
					stack.push(piece.toString());
				} else {
					stack.push(currentItem);
				}
			}
		} catch (RuntimeException e) {
			stack.clear();
			stack.push("#ERROR#");
		}
		
		return stack.empty() ? "" : stack.pop();
	}

}
