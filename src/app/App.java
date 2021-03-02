package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import AsymmetricTSP.Transform;
import graph.City;
import graph.Edge;
import graph.Manager;
import graphics.MainFrame;
import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearIntExpr;
import ilog.concert.IloLinearNumExpr;
import ilog.cplex.IloCplex;

public class App {

    public static void main(String[] args) {
//        first_method();
        second_method(); // 模型求解
//        third_method(); // 转换求解
    }

    // 输入坐标算例的对称TSP问题
    private static void first_method(){
        double timeStart = System.currentTimeMillis();

        // 读取数据
        String[] args = new String[]{"--instancePath", "./input/berlin52.csv", "--maximumRead", "52"};

        HashMap<String, String> hashArgs = getHashArgs(args);

        FileManager fileManager = new FileManager(hashArgs);

        ArrayList<City> data = fileManager.readInstance();

        // 初始化stacks
        ArrayList<Edge> tour = new ArrayList<Edge>();
        ArrayList<Stack<Edge>> stacks = new ArrayList<Stack<Edge>>();
        Manager manager = new Manager(tour, stacks);

        double[][] distance = manager.getAdjMatrix(data);

//		MainFrame mainFrame = new MainFrame(data, tour, distance, hashArgs.get("--imagePath"),
//				Integer.parseInt(hashArgs.get("--index")), fileManager.getMaxX(), fileManager.getMaxY());
//		mainFrame.visualize();

        ConstraintFactory constraintFactory = new ConstraintFactory();

        int count = 0;
        while (true) {
            try {
                IloCplex model = new IloCplex();

                // define variables
                IloIntVar[][] x = new IloIntVar[data.size()][data.size()];
                for (int i = 0; i < x.length; i++) {
                    for (int j = 0; j < x.length; j++) {
                        x[i][j] = model.boolVar("X[" + i + ", " + j + "]");
                    }
                }

                // one has only a city to go, and should
                for (int i = 0; i < x.length; i++) {
                    IloLinearIntExpr r = model.linearIntExpr();// TODO
                    for (int j = 0; j < x.length; j++) {
                        r.addTerm(1, x[i][j]);
                    }
                    model.addEq(r, 1);
                }

                // one can only arrive to one city at a time, and should
                for (int j = 0; j < x.length; j++) {
                    IloLinearIntExpr r = model.linearIntExpr();// TODO
                    for (int i = 0; i < x.length; i++) {
                        r.addTerm(1, x[i][j]);
                    }
                    model.addEq(r, 1);
                }

                // one cannot go to the same city as he is
                for (int i = 0; i < x.length; i++) {
                    IloLinearIntExpr r = model.linearIntExpr();
                    r.addTerm(1, x[i][i]);
                    model.addEq(r, 0);
                }

                // add cycle restrictions
                for (Stack<Edge> stack : stacks) {
//					stack.forEach((edge) -> System.out.println(edge.getFrom() + "->" + edge.getTo()));
                    constraintFactory.cycleRestrictions(model, x, stack);
                }

                // one should complete the tour within the smallest distance possible
                IloLinearNumExpr z = model.linearNumExpr();
                for (int i = 0; i < x.length; i++) {
                    for (int j = 0; j < x.length; j++) {
                        if (i == j)
                            continue;
                        z.addTerm(distance[i][j], x[i][j]);
                    }
                }
                model.addMinimize(z);
//				System.out.println(model.getModel());

                tour.clear();

                if (model.solve()) {
                    // get tour
                    for (int i = 0; i < x.length; i++) {
                        for (int j = 0; j < x.length; j++) {
                            if (model.getValue(x[i][j]) >= 0.5) {
                                tour.add(new Edge(i, j));
                            }
                        }
                    }
                    // repaint tour
//					mainFrame.graphDraw.repaint();
                } else {
                    System.err.println("Boi, u sick!");
                    System.exit(1);
                }

                System.out.println("Value = " + model.getObjValue());
                boolean done = manager.recycle(false);
                if (done) {
                    break;
                }

                count++;
            } catch (IloException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        System.out.println("Time since beginning =  " + ((System.currentTimeMillis() - timeStart) / 1000) + " seconds");
        System.out.println("<<<<<<<<<<<<< Finally we get opt solution >>>>>>>>>>>>>");
        System.out.println("With " + count + " iterations.");
        System.out.println("Time Elapsed =  " + ((System.currentTimeMillis() - timeStart) / 1000) + " seconds");
        System.out.println("Done");
//		mainFrame.repaint();
    }


    // 输入距离矩阵算例的非对称TSP问题
    private static void second_method(){
        double timeStart = System.currentTimeMillis();

        // 读取数据
        String[] args = new String[]{"--instancePath", "./input/realfiles/forftp/atex0"};

        HashMap<String, String> hashArgs = getHashArgs(args);

        FileManager2 fileManager = new FileManager2(hashArgs);

        ArrayList<City> data = fileManager.readCities(false);

        // 初始化stacks
        ArrayList<Edge> tour = new ArrayList<Edge>();
        ArrayList<Stack<Edge>> stacks = new ArrayList<Stack<Edge>>();
        Manager manager = new Manager(tour, stacks);

        double[][] distance = fileManager.readAdjMatrix();

        ConstraintFactory constraintFactory = new ConstraintFactory();

        int count = 0;
        double bestTourCost = 0;
        int[] bestTour = new int[fileManager.cusNr + 1];

        while (true) {
            try {
                IloCplex model = new IloCplex();

                // define variables
                IloIntVar[][] x = new IloIntVar[data.size()][data.size()];
                for (int i = 0; i < x.length; i++) {
                    for (int j = 0; j < x.length; j++) {
                        x[i][j] = model.boolVar("X[" + i + ", " + j + "]");
                    }
                }

                // one has only a city to go, and should
                for (int i = 0; i < x.length; i++) {
                    IloLinearIntExpr r = model.linearIntExpr();// TODO
                    for (int j = 0; j < x.length; j++) {
                        r.addTerm(1, x[i][j]);
                    }
                    model.addEq(r, 1);
                }

                // one can only arrive to one city at a time, and should
                for (int j = 0; j < x.length; j++) {
                    IloLinearIntExpr r = model.linearIntExpr();// TODO
                    for (int i = 0; i < x.length; i++) {
                        r.addTerm(1, x[i][j]);
                    }
                    model.addEq(r, 1);
                }

                // one cannot go to the same city as he is
                for (int i = 0; i < x.length; i++) {
                    IloLinearIntExpr r = model.linearIntExpr();
                    r.addTerm(1, x[i][i]);
                    model.addEq(r, 0);
                }

                // add cycle restrictions
                for (Stack<Edge> stack : stacks) {
//					stack.forEach((edge) -> System.out.println(edge.getFrom() + "->" + edge.getTo()));
                    constraintFactory.cycleRestrictions(model, x, stack);
                }

                // one should complete the tour within the smallest distance possible
                IloLinearNumExpr z = model.linearNumExpr();
                for (int i = 0; i < x.length; i++) {
                    for (int j = 0; j < x.length; j++) {
                        if (i == j)
                            continue;
                        z.addTerm(distance[i][j], x[i][j]);
                    }
                }
                model.addMinimize(z);
//				System.out.println(model.getModel());

                tour.clear();

                if (model.solve()) {
                    // get tour
                    for (int i = 0; i < x.length; i++) {
                        for (int j = 0; j < x.length; j++) {
                            if (model.getValue(x[i][j]) >= 0.5) {
                                tour.add(new Edge(i, j));
                            }
                        }
                    }
                    // repaint tour
//					mainFrame.graphDraw.repaint();
                } else {
                    System.err.println("Boi, u sick!");
                    System.exit(1);
                }

                System.out.println("Value = " + model.getObjValue());

                boolean done = manager.recycle(false);
                if (done) {
                    int pre = 0, pos = 0;
                    bestTour[0] = 0;
                    pos++;
                    while (true){
                        if(pre == 0 && pos > 1){
                            break;
                        }
                        for (int i = 0; i < x.length; i++) {
                            if (model.getValue(x[pre][i]) >= 0.5) {
                                bestTour[pos] = i;
                                pre = i;
                                pos++;
                                break;
                            }
                        }
                    }

                    for(int i = 0; i < bestTour.length - 1; i++){
                        bestTourCost += distance[bestTour[i]][bestTour[i + 1]];
                    }

                    break;
                }

                count++;
            } catch (IloException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        System.out.println("Time since beginning =  " + ((System.currentTimeMillis() - timeStart) / 1000) + " seconds");
        System.out.println("<<<<<<<<<<<<< Finally we get opt solution >>>>>>>>>>>>>");
        System.out.println("With " + count + " iterations.");
        System.out.println("Time Elapsed =  " + ((System.currentTimeMillis() - timeStart) / 1000) + " seconds");
        System.out.println("Best tour : ");
        for(int i = 0; i < bestTour.length; i++){
            System.out.print(bestTour[i] + " ");
        }
        System.out.println("\nBest cost = " + bestTourCost);
        System.out.println("Done");
//		mainFrame.repaint();
    }


    // 将非对称TSP问题转化为对称TSP问题求解
    private static void third_method(){
        double timeStart = System.currentTimeMillis();

        // 读取数据
        String[] args = new String[]{"--instancePath", "./input/realfiles/forftp/atex0"};

        HashMap<String, String> hashArgs = getHashArgs(args);

        FileManager2 fileManager = new FileManager2(hashArgs);

        ArrayList<City> data = fileManager.readCities(true);

        // 初始化stacks
        ArrayList<Edge> tour = new ArrayList<Edge>();
        ArrayList<Stack<Edge>> stacks = new ArrayList<Stack<Edge>>();
        Manager manager = new Manager(tour, stacks);

        double[][] distance2 = fileManager.readAdjMatrix();
        double[][] distance = Transform.Transforming(distance2);

        ConstraintFactory constraintFactory = new ConstraintFactory();

        int count = 0;
        double bestTourCost = 0;
        int[] bestTour = new int[fileManager.cusNr * 2 + 1]; // 模型解出的最优解
        int[] bestTour2 = new int[fileManager.cusNr + 1]; // 转化回的最优解

        while (true) {
            try {
                IloCplex model = new IloCplex();

                // define variables
                IloIntVar[][] x = new IloIntVar[data.size()][data.size()];
                for (int i = 0; i < x.length; i++) {
                    for (int j = 0; j < x.length; j++) {
                        x[i][j] = model.boolVar("X[" + i + ", " + j + "]");
                    }
                }

                // one has only a city to go, and should
                for (int i = 0; i < x.length; i++) {
                    IloLinearIntExpr r = model.linearIntExpr();// TODO
                    for (int j = 0; j < x.length; j++) {
                        r.addTerm(1, x[i][j]);
                    }
                    model.addEq(r, 1);
                }

                // one can only arrive to one city at a time, and should
                for (int j = 0; j < x.length; j++) {
                    IloLinearIntExpr r = model.linearIntExpr();// TODO
                    for (int i = 0; i < x.length; i++) {
                        r.addTerm(1, x[i][j]);
                    }
                    model.addEq(r, 1);
                }

                // one cannot go to the same city as he is
                for (int i = 0; i < x.length; i++) {
                    IloLinearIntExpr r = model.linearIntExpr();
                    r.addTerm(1, x[i][i]);
                    model.addEq(r, 0);
                }

                // add cycle restrictions
                for (Stack<Edge> stack : stacks) {
//					stack.forEach((edge) -> System.out.println(edge.getFrom() + "->" + edge.getTo()));
                    constraintFactory.cycleRestrictions(model, x, stack);
                }

                // one should complete the tour within the smallest distance possible
                IloLinearNumExpr z = model.linearNumExpr();
                for (int i = 0; i < x.length; i++) {
                    for (int j = 0; j < x.length; j++) {
                        if (i == j)
                            continue;
                        z.addTerm(distance[i][j], x[i][j]);
                    }
                }
                model.addMinimize(z);
//				System.out.println(model.getModel());

                tour.clear();

                if (model.solve()) {
                    // get tour
                    for (int i = 0; i < x.length; i++) {
                        for (int j = 0; j < x.length; j++) {
                            if (model.getValue(x[i][j]) >= 0.5) {
                                tour.add(new Edge(i, j));
                            }
                        }
                    }
                    // repaint tour
//					mainFrame.graphDraw.repaint();
                } else {
                    System.err.println("Boi, u sick!");
                    System.exit(1);
                }

                System.out.println("Value = " + model.getObjValue());

                boolean done = manager.recycle(false);
                if (done) {
                    int pre = 0, pos = 0;
                    bestTour[0] = 0;
                    pos++;
                    while (true){
                        if(pre == 0 && pos > 1){
                            break;
                        }
                        for (int i = 0; i < x.length; i++) {
                            if (model.getValue(x[pre][i]) >= 0.5) {
                                bestTour[pos] = i;
                                pre = i;
                                pos++;
                                break;
                            }
                        }
                    }

                    bestTour2 = Transform.TransformTour(bestTour);
                    for(int i = 0; i < bestTour2.length - 1; i++){
                        bestTourCost += distance2[bestTour2[i]][bestTour2[i + 1]];
                    }

                    break;
                }

                count++;
            } catch (IloException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        System.out.println("Time since beginning =  " + ((System.currentTimeMillis() - timeStart) / 1000) + " seconds");
        System.out.println("<<<<<<<<<<<<< Finally we get opt solution >>>>>>>>>>>>>");
        System.out.println("With " + count + " iterations.");
        System.out.println("Time Elapsed =  " + ((System.currentTimeMillis() - timeStart) / 1000) + " seconds");
        System.out.println("Best tour : ");
        for(int i = 0; i < bestTour2.length; i++){
            System.out.print(bestTour2[i] + " ");
        }
        System.out.println("\nBest cost = " + bestTourCost);
        System.out.println("Done");
//		mainFrame.repaint();
    }


    private static HashMap<String, String> getHashArgs(String[] args) {
        if (args.length % 2 != 0) {
            System.out.println("Wrong number of arguments.");
            System.exit(1);
        }

        HashMap<String, String> hashArgs = new HashMap<String, String>();
        for (int i = 0; i < args.length; i += 2) {
            hashArgs.put(args[i], args[i + 1]);
        }

        return hashArgs;
    }
}