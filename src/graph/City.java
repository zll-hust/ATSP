package graph;

public class City {

	private double x;
	private double y;
	private int index;

	public City(int index) {
		this.index = index;
	}

	public City(int index, double x, double y) {
		this.index = index;
		this.x = x;
		this.y = y;
	}

	public int getIndex() {
		return index;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

}
