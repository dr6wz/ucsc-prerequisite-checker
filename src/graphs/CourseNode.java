package graphs;
import java.util.ArrayList;

public class CourseNode extends Node {
	private String number;
	private String title;
	private boolean active;
	private boolean miscPrereqs;
	
	CourseNode(String number, String title, boolean misc) {
		this.number = number;
		this.title = title;
		this.miscPrereqs = misc;
		this.makeInactive();
	}
	
	String getNumber() {
		return number;
	}
	
	String getTitle() {
		return title;
	}
	
	boolean isActive() {
		return active;
	}
	
	boolean prereqsMet() {
		for (int a = 0; a < adjacencies.size(); a += 1) {
			Node prereq = adjacencies.get(a);
			if (prereq instanceof CourseNode) {
				boolean met = ((CourseNode) prereq).isActive();
				if (!met) {
					return false;
				}
			} else if (prereq instanceof LogicalNode) {
				boolean met = ((LogicalNode) prereq).isMet();
				if (!met) {
					return false;
				}
			}
		}
		return true;
	}
	
	void addPrereq(Node n) {
		boolean success = adjacencies.add(n);
		if (!success) {
			System.out.println("addPrereq() warning: Unable to add node "
					+ n + " to graph.");
		}
	}
	
	void clearPrereqs() {
		clear();
	}
	
	void makeActive() {
		active = true;
	}
	
	void makeInactive() {
		active = false;
	}
	
	// to-do
	protected void printAdjacencies() {
		
	}
	
	public String toString() {
		return number + ": " + active;
	}
}
