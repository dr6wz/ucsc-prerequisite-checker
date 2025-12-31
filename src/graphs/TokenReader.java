package graphs;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class TokenReader {
	static final int PLAIN_STATEMENT = 0;
	static final int OR_STATEMENT = 1;
	static final int AND_STATEMENT = 2;
	static final int N_OF_STATEMENT = 3;

	static String trimParentheses(String s) {
		if (s.length() <= 0) {
			return "";
		}
		String str = "" + s;
		while (str.charAt(0) == '(' && closingParenthesis(str, 0) == str.length() - 1) {
			str = str.substring(1, str.length() - 1);
		}
		return str;
	}
	
	/*
	 * returns the index of the first opening parenthesis of s
	 * starting at index i
	 */
	static int openingParenthesis(String s, int i) {
		return s.indexOf('(', i);
	}
	
	/**
	 * returns the index of the closing parenthesis associated
	 * with the opening parenthesis at index i
	 * @precondition: s is a parenthesis-balanced string 
	 */
	static int closingParenthesis(String s, int i) {
		int opens = 0;
		int closes = 0;
		for (int a = i + 1; a < s.length(); a += 1) {
			char c = s.charAt(a);
			if (c == '(') {
				opens += 1;
			} else if (c == ')') { 
				if (opens == closes) {
					return a;
				} else {
					closes += 1;
				}
			}
		}
		return -1;
	}
	
	/**
	 * returns an arraylist of integers where items in
	 * even indices represent the indices of opening parentheses
	 * of the outermost level in string s
	 * and items in odd indices represent indices of closing parentheses
	 * of the outermost level in string s
	 * 
	 * example: string "(((())))()()()" results in arraylist
	 * {0, 7, 8, 9, 10, 11, 12, 13} being returned
	 * 
	 * @precondition: s is balanced
	 */
	static ArrayList<Integer> outerParenthesisIndices(String s) {
		ArrayList<Integer> indices = new ArrayList<Integer>();
		int openingIndex = openingParenthesis(s, 0);

		while (openingIndex >= 0) {
			indices.add(openingIndex);
			int closingIndex = closingParenthesis(s, openingIndex);
			indices.add(closingIndex);
			openingIndex = openingParenthesis(s, closingIndex);
		}
		
		return indices;
	}
	
	/**
	 * returns only the outer level content of s
	 * example: "(AM 10 || MATH 21) && (AM 20 || MATH 24)"
	 * results in " && " being returned
	 */
	static String outerLevel(String s, ArrayList<Integer> indices) {
		if (indices.isEmpty()) {
			return s;
		}
		// do the beginning "outer" part of the string
		String str = s.substring(0, indices.get(0));
		// System.out.println(indices);
		// do the "interior" part of the string
		for (int a = 1; a < indices.size() - 1; a += 2) {
			str += s.substring(indices.get(a) + 1, indices.get(a + 1));
		}
		// do the ending "outer" part of the string
		str += s.substring(indices.get(indices.size() - 1) + 1);
		return str;
	}
	
	/**
	 * @precondition s is either an AND or OR statement and isn't a plain statement
	 * @param s the string to be split
	 * @param type the type of statement represented by s (AND or OR)
	 * @return a string array containing the individual tokens of s
	 * @throws Exception
	 */
	static String[] split(String s, int type) throws Exception {
		if (s.length() < 2) {
			throw new Exception("split() error: string must be at least"
					+ "of length 2");
		}
		String divider;
		int dividerLength = 0;
		if (type == AND_STATEMENT) {
			divider = "&&";
			dividerLength = 2;
		} else if (type == OR_STATEMENT) {
			divider = "||";
			dividerLength = 2;
		} else if (type == N_OF_STATEMENT) {
			Pattern pattern = Pattern.compile("([\\d]+)\\?\\?");
			Matcher matcher = pattern.matcher(s);
			matcher.find();
			divider = matcher.group();
			dividerLength = divider.length();
		} else {
			return null;
		}
		ArrayList<Integer> dividerIndices = new ArrayList<Integer>();
		int i = 0;
		while (i < s.length() - (dividerLength - 1)) {
			char c = s.charAt(i);
			String sub = s.substring(i, i + dividerLength);
			if (c == '(') {
				i = closingParenthesis(s, i);
			} else if (sub.equals(divider)) {
				dividerIndices.add(i);
				i += dividerLength;
			} else {
				i += 1;
			}
		}
		ArrayList<String> tokens = new ArrayList<String>();
		// adds the token just before the first divider index
		String tok = s.substring(0, dividerIndices.get(0)).trim();
		tokens.add(tok);
		// adds the "interior" tokens
		int a = 0;
		while (a < dividerIndices.size() - 1) {
			int begin = dividerIndices.get(a);
			int end = dividerIndices.get(a + 1);
			tok = s.substring(begin + dividerLength, end).trim();
			tokens.add(tok);
			a += 1;
		}
		// adds the token just after the last divider index
		tok = s.substring(dividerIndices.get(dividerIndices.size() - 1) + dividerLength).trim();
		tokens.add(tok);
		String[] tokensArr = new String[tokens.size()];
		for (a = 0; a < tokensArr.length; a += 1) {
			tokensArr[a] = tokens.get(a);
		}
		return tokensArr;
	}
	
	// returns true if the string is parenthesis-balanced
	// false otherwise
	static boolean isBalanced(String s) {
		int opens = 0;
		int closes = 0;
		for (int a = 0; a < s.length(); a += 1) {
			char c = s.charAt(a);
			if (c == '(') {
				opens += 1;
			} else if (c == ')') {
				closes += 1;
				if (closes > opens) {
					return false;
				}
			}
		}
		return opens == closes;
	}
	
	/**
	 * 
	 * @precondition only the outer level of the string is preserved
	 */
	static int deduceType(String s) {
		Pattern pattern = Pattern.compile("([\\d]+)\\?\\?");
		Matcher matcher = pattern.matcher(s);
		if (s.indexOf("&&") >= 0) {
			return AND_STATEMENT;
		} else if (s.indexOf("||") >= 0) {
			return OR_STATEMENT;
		} else if (matcher.find()) {
			return N_OF_STATEMENT;
		} else {
			return PLAIN_STATEMENT;
		}
	}
}
