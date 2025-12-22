package graphs;
import java.util.ArrayList;

public abstract class Node {
	protected ArrayList<Node> adjacencies = new ArrayList<Node>();
	
	protected abstract void printAdjacencies();
	
	protected void addAdjacency(Node n) {
		boolean success = adjacencies.add(n);
		if (!success) {
			System.out.println("addPrereq() warning: Unable to add node "
					+ n + " to graph.");
		}
	}
	
	// clears the node of its adjacencies
	protected void clear() {
		adjacencies.clear();
	}
}