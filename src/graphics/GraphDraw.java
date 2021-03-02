package graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import graph.City;
import graph.Edge;

public class GraphDraw extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ArrayList<City> data;
	ArrayList<Edge> tour;
	double[][] distanceFromTo;
	int index;
	double maxX;
	double maxY;

	GraphDraw(ArrayList<City> data, ArrayList<Edge> tour, double[][] distanceFromTo, int index, double maxX, double maxY) {
		this.data = data;
		this.tour = tour;
		this.distanceFromTo = distanceFromTo;
		this.index = index;
		this.maxX = maxX+50;
		this.maxY = maxY+50;

		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setPreferredSize(super.getSize());
		this.setBackground(Color.white);
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		double scaleX = super.getWidth() / maxX;
		double scaleY = super.getHeight() / maxY;

		double rx = 5;
		double ry = 5;
		double base = 5;

		// draw cities
		for (City city : data) {

			double placeX = city.getX() * scaleX - ((rx) / 2);
			double placeY = city.getY() * scaleY - ((ry) / 2);
//			double placeX = ctx * scaleX - ((rx) / 2);
//			double placeY = cty * scaleY - ((ry) / 2);
			
			if (city.getIndex() != this.index) {
				double placeXBase = placeX - base / 2;
				double placeYBase = placeY - base / 2;
				Ellipse2D ellipse2dBase = new Ellipse2D.Double(placeXBase, placeYBase, rx + base, ry + base);

				// draw city basis
				g2d.setColor(Color.black);
				g2d.fill(ellipse2dBase);
				g2d.draw(ellipse2dBase);

				Ellipse2D ellipse2d = new Ellipse2D.Double(placeX, placeY, rx, ry);

				// draw points as cities
				g2d.setColor(Color.red);
				g2d.fill(ellipse2d);
				g2d.draw(ellipse2d);
			} else {
				placeX = city.getX() * scaleX - ((rx + base) / 2);
				placeY = city.getY() * scaleY - ((ry + base) / 2);
				Ellipse2D ellipse2d = new Ellipse2D.Double(placeX, placeY, rx + base, ry + base);

				// draw points as cities
				g2d.setColor(Color.magenta);
				g2d.fill(ellipse2d);
				g2d.draw(ellipse2d);
			}

			// draw city index number
			g2d.setColor(Color.blue);
			g2d.setFont(new Font("Monospaced", Font.BOLD, 12));
			g2d.drawString(String.valueOf(city.getIndex()), (float) (placeX + base * 2), (float) (placeY + base * 2));
		}

		// draw edges
		for (Edge edge : tour) {
			int idxFrom = edge.getFrom();
			int idxTo = edge.getTo();

			double fromX = this.data.get(idxFrom).getX() * scaleX;
			double fromY = this.data.get(idxFrom).getY() * scaleY;

			double toX = this.data.get(idxTo).getX() * scaleX;
			double toY = this.data.get(idxTo).getY() * scaleY;

			Line2D line2d = new Line2D.Double(fromX, fromY, toX, toY);

			g2d.setColor(Color.gray);

			// draw edge
			g2d.setStroke(new BasicStroke(2));
			g2d.draw(line2d);

			// draw distance

		}
	}
}
