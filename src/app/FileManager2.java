package app;

import graph.City;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author： zll-hust
 * @date： 2021/3/2 10:16
 * @description： TODO
 */
public class FileManager2 {
    private HashMap<String, String> args;
    public int cusNr;

    FileManager2(HashMap<String, String> args) {
        this.args = args;
    }

    public ArrayList<City> readCities(boolean isAsymmetirc) {
        ArrayList<City> instance = new ArrayList<City>();

        try (BufferedReader br = new BufferedReader(new FileReader(new File(args.get("--instancePath"))))) {
            String line = br.readLine();
            String[] parts = line.split(" ");
            cusNr = Integer.parseInt(parts[0]);
            int count = 0;
            if(isAsymmetirc){
                for (int i = 0; i < cusNr * 2; i++) {
                    instance.add(new City(count++));
                }
            }else{
                for (int i = 0; i < cusNr; i++) {
                    instance.add(new City(count++));
                }
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return instance;
    }

    public double[][] readAdjMatrix() {
        double[][] matrix = new double[cusNr][cusNr];

        try (BufferedReader br = new BufferedReader(new FileReader(new File(args.get("--instancePath"))))) {
            br.readLine();
            br.readLine();
            String line;
            for (int i = 0; i < cusNr; i++) {
                line = br.readLine();
                String[] parts = line.split(" ");
                for (int j = 0; j < cusNr; j++) {
                    matrix[i][j] = Integer.parseInt(parts[j]);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return matrix;
    }

}
