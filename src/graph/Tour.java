package graph;

import java.util.ArrayList;
import java.util.LinkedList;

public class Tour {

	LinkedList<Integer> circuit;
	ArrayList<Edge> edges;

	Tour(ArrayList<Edge> edges, int start) {
		this.edges = edges;
	}
}
