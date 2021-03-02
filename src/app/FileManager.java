package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import graph.City;

public class FileManager {

	private HashMap<String, String> args;
	private double maxX;
	private double maxY;

	FileManager(HashMap<String, String> args) {
		this.args = args;
		this.maxX = 0;
		this.maxY = 0;
	}
	
	public double getMaxX() {
		return this.maxX;
	}
	public double getMaxY() {
		return this.maxY;
	}

	public ArrayList<City> readInstance() {
		ArrayList<City> instance = new ArrayList<City>();

		try (BufferedReader br = new BufferedReader(new FileReader(new File(args.get("--instancePath"))))) {
			br.readLine();
			String line;
			int count = 0;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				double x = Double.parseDouble(parts[0]);
				double y = Double.parseDouble(parts[1]);
				
				if(maxX < x) {
					maxX = x;
				}
				
				if(maxY < y) {
					maxY = y;
				}
				
				instance.add(new City(count++, x, y));
				if (count == Integer.parseInt(this.args.get("--maximumRead"))) {
					break;
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		return instance;
	}

}