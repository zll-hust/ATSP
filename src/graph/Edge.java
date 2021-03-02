package graph;

public class Edge implements Comparable<Edge> {

	private int from;
	private int to;

	public Edge(int from, int to) {
		super();
		this.from = from;
		this.to = to;
	}

	public Edge(Edge other) {
		this.from = other.getFrom();
		this.to = other.getTo();
	}

	public int getFrom() {
		return from;
	}

	public int getTo() {
		return to;
	}

	@Override
	public int compareTo(Edge o) {
		if (this.from == o.from) {
			if (this.to == o.to) {
				return 0;
			}
		}
		return 1;
	}

	@Override
	public boolean equals(Object o) {
		Edge other = (Edge) o;
		if (this.from == other.from) {
			if (this.to == other.to) {
				return true;
			}
		}
		return false;
	}

}
