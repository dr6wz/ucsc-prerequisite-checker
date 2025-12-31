package graphs;
import java.util.ArrayList;

/**
 * series of unit tests for the TokenReader helper class
 * and the CourseGraph class
 * @author dr6wz
 *
 */
public class HelpersTest {
	public static void main(String[] args) {
		testPrereqsAm114();
		testParenthesisTrim();
		testOuterParenthesisIndices();
		testOuterLevel();
		testSetPrereq();
		testSplitNOf();
	}
	
	private static void testSetPrereq() {
		CourseGraph graph = new CourseGraph();
		String[] courseNumbers = {
				"AM 20",
				"AM 30",
				"MATH 24",
				"PHYS 116A",
				"AM 129",
				"CSE 30",
				"AM 160",
		};
		for (String s: courseNumbers) {
			graph.addCourse(s, "", false);
		}
		graph.setPrereqs("AM 160", "((AM 20 && AM 30) || MATH 24 || PHYS 116A) && (AM 129 || CSE 30)");
	}
	
	private static void testParenthesisTrim() {
		String s1 = "(((((AM 100 || AM 30) && (AM 10 || MATH 21)))))";
		assert TokenReader.trimParentheses(s1).equals("(AM 100 || AM 30) && (AM 10 "
				+ "|| MATH 21)");
	}
	
	private static void testOuterParenthesisIndices() {
		String s = "(((())))()()()";
		System.out.println(TokenReader.outerParenthesisIndices(s));
	}
	
	private static void testOuterLevel() {
		String s = "(AM 10 || MATH 21) && (AM 20 || MATH 24)";
		ArrayList<Integer> indices = TokenReader.outerParenthesisIndices(s);
		System.out.println(TokenReader.outerLevel(s, indices));
		// should be " && "
	}
	
	private static void testPrereqsAm114() {
		CourseGraph cg = new CourseGraph();
		String[] courseNumbers = {
			"AM 10",
			"AM 20",
			"AM 30",
			"MATH 21",
			"MATH 24",
			"MATH 22",
			"MATH 23A",
			"PHYS 116A",
			"AM 114",
		};
		for (String s: courseNumbers) {
			cg.addCourse(s, "", false);
		}
		
		// seeing if the core logic works with am 114
		// (apart from token parsing)
		CourseNode am114 = cg.getCourseNode("AM 114");
		OrNode linear = new OrNode();
		linear.addAdjacency(cg.getCourseNode("AM 10"));
		linear.addAdjacency(cg.getCourseNode("MATH 21"));
		OrNode diffEq = new OrNode();
		diffEq.addAdjacency(cg.getCourseNode("AM 20"));
		diffEq.addAdjacency(cg.getCourseNode("MATH 24"));
		OrNode vectorCalc = new OrNode();
		vectorCalc.addAdjacency(cg.getCourseNode("AM 30"));
		vectorCalc.addAdjacency(cg.getCourseNode("MATH 22"));
		vectorCalc.addAdjacency(cg.getCourseNode("MATH 23A"));
		OrNode outer = new OrNode();
		outer.addAdjacency(cg.getCourseNode("PHYS 116A"));
		AndNode alt = new AndNode();
		alt.addAdjacency(linear);
		alt.addAdjacency(diffEq);
		alt.addAdjacency(vectorCalc);
		outer.addAdjacency(alt);
		
		am114.addPrereq(outer);

		cg.getCourseNode("AM 10").makeActive();
		cg.getCourseNode("AM 20").makeActive();
		cg.getCourseNode("MATH 23A").makeActive();
		cg.getCourseNode("PHYS 116A").makeActive();
		
		assert am114.prereqsMet();
		// yes, it works
	}
	
	private static void testSplitNOf() {
		try {
			String str = "ART 15 3?? ART 20 3?? ART 30 3?? ART 40 3?? ART 50";
			int type = TokenReader.deduceType(str);
			String[] tokens = TokenReader.split(str, type);
			for (String s: tokens) {
				System.out.println(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}