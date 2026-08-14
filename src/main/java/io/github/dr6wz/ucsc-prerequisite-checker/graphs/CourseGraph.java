package graphs;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Set;
import java.util.ArrayList;

public class CourseGraph extends Graph {
	private TreeMap<String, Node> nodes;

	public CourseGraph() {
		nodes = new TreeMap<String, Node>();
	}

	public void addCourse(String number, String title, boolean misc) {
		CourseNode course = new CourseNode(number, title, misc);
		nodes.put(number, course);
	}

	public void setPrereqs(String courseNumber, String prereqs) {
		// clears existing prerequisites
		CourseNode course = (CourseNode) nodes.get(courseNumber);
		course.clearPrereqs();

		try {
			if (!prereqs.equals("")) {
				setPrereqs(course, prereqs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// parses the string prereqs and recursively sets prerequisites
	// according to the tokens in prereqs
	private void setPrereqs(Node course, String prereqs) throws Exception {
		if (!TokenReader.isBalanced(prereqs)) {
			throw new Exception("Unbalanced string");
		}
		// trims the string of any excess outer parentheses
		prereqs = TokenReader.trimParentheses(prereqs);
		ArrayList<Integer> parenthesisIndices = TokenReader.outerParenthesisIndices(prereqs);
		// removes all tokens surrounded in parentheses so that
		// the outer type of the string can be deduced
		// (e.g. AND, OR, plain)
		String topLevel = TokenReader.outerLevel(prereqs, parenthesisIndices);
		// represents the string type
		int type = TokenReader.deduceType(topLevel);
		
		switch (type) {
		case TokenReader.PLAIN_STATEMENT:
			topLevel = topLevel.trim();
			CourseNode preReq = (CourseNode) nodes.get(topLevel);
			course.addAdjacency(preReq);
			break;
		case TokenReader.AND_STATEMENT:
			String[] tokens = TokenReader.split(prereqs, type);
			AndNode and = new AndNode();
			for (int a = 0; a < tokens.length; a += 1) {
				setPrereqs(and, tokens[a]);
			}
			course.addAdjacency(and);
			break;
		case TokenReader.OR_STATEMENT:
			String[] tok = TokenReader.split(prereqs, type);
			OrNode or = new OrNode();
			for (int a = 0; a < tok.length; a += 1) {
				setPrereqs(or, tok[a]);
			}
			course.addAdjacency(or);
			break;
		case TokenReader.N_OF_STATEMENT:
			String[] toks = TokenReader.split(prereqs, type);
			Pattern pattern = Pattern.compile("([\\d]+)\\?\\?");
			Matcher matcher = pattern.matcher(topLevel);
			matcher.find();
			int n = Integer.parseInt(matcher.group(1));
			NOfNode nOf = new NOfNode(n);
			for (int a = 0; a < toks.length; a += 1) {
				setPrereqs(nOf, toks[a]);
			}
			course.addAdjacency(nOf);
			break;
		}
	}
	
	// sets the given course's active flag to true
	public void activateCourse(String number) {
		CourseNode course = (CourseNode) nodes.get(number);
		course.makeActive();
	}
	
	// sets the given course's active flag to false
	public void deactivate(String number) {
		CourseNode course = (CourseNode) nodes.get(number);
		course.makeInactive();
	}
	
	// prints the graph's nodes to the console
	public void printNodes() {
		Set<String> keys = nodes.keySet();
		for (String s: keys) {
			System.out.println(nodes.get(s));
		}
	}

	CourseNode getCourseNode(String number) {
		return (CourseNode) nodes.get(number);
	}
}