package graphs;

public class NOfNode extends LogicalNode {
	private final int n;
	
	NOfNode(int n) {
		this.n = n;
	}
	
	boolean isMet() {
		int count = 0;
		for (int a = 0; a < adjacencies.size(); a += 1) {
			Node node = adjacencies.get(a);
			if (node instanceof CourseNode) {
				CourseNode course = (CourseNode) node;
				if (course.isActive()) {
					count += 1;
				}
			} else if (node instanceof LogicalNode) {
				LogicalNode logical = (LogicalNode) node;
				if (logical.isMet()) {
					count += 1;
				}
			}
			if (count >= n) {
				return true;
			}
		}
		return false;
	}
	
	// to-do
	protected void printAdjacencies() {
		
	}
}