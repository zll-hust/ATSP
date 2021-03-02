package graphics;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import graph.City;
import graph.Edge;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GraphDraw graphDraw;

	public MainFrame(ArrayList<City> data, ArrayList<Edge> tour, double[][] distanceFromTo, String imagePath,
			int index,double maxX, double maxY) {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(new Dimension(1920, 1200));
		this.setLayout(new BorderLayout());
		this.setTitle("OR TSP CPLEX");

		this.graphDraw = new GraphDraw(data, tour, distanceFromTo, index,maxX, maxY);

		JLabel labelTitle = new JLabel("Tour Graph");
		labelTitle.setHorizontalAlignment(SwingConstants.CENTER);

		JButton buttonSave = new JButton("Save Image");
		buttonSave.setHorizontalAlignment(SwingConstants.CENTER);
		buttonSave.addActionListener(new ImageSaveListener(this, imagePath));

		this.setLocationRelativeTo(null);
		this.add(labelTitle, BorderLayout.PAGE_START);
		this.add(this.graphDraw, BorderLayout.CENTER);
		this.add(buttonSave, BorderLayout.PAGE_END);
	}

	public void visualize() {
		// TODO Auto-generated method stub
		this.setVisible(true);
	}
}
