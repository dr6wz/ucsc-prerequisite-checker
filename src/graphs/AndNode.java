package graphs;

public class AndNode extends LogicalNode {
	boolean isMet() {
		for (int a = 0; a < adjacencies.size(); a += 1) {
			Node adjItem = adjacencies.get(a);
			if (adjItem instanceof LogicalNode) {
				boolean met = ((LogicalNode) adjItem).isMet();
				if (!met) {
					return false;
				}
			} else if (adjItem instanceof CourseNode) {
				boolean active = ((CourseNode) adjItem).isActive();
				if (!active) {
					return false;
				}
			} else {
				System.out.println("Warning: iterating over a Node that is"
						+ "neither a LogicalNode nor a CourseNode.");
			}
		}
		return true;
	}

	// to-do
	protected void printAdjacencies() {
		if (!adjacencies.isEmpty()) {

		} else {
			System.out.println("Empty AND node");
		}
	}
}