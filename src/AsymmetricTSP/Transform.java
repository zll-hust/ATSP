package AsymmetricTSP;

import java.util.Arrays;

/**
 * @author： zll-hust
 * @date： 2021/3/2 11:11
 * @description： TODO
 */
public class Transform {
    private static int M = 10000;

    public static double[][] Transforming(double[][] C) {
        double[][] new_matrix = new double[C.length * 2][C.length * 2];
        for(int i = 0; i < new_matrix.length; i++){
            Arrays.fill(new_matrix[i], M);
        }
        double[][] Ct = reverse(C);
        for (int i = 0; i < C.length; i++) {
            for (int j = 0; j < C.length; j++) {
                if(i == j){
                    new_matrix[i][j + C.length] = -1 * M;
                }else{
                    new_matrix[i][j + C.length] = Ct[i][j];
                }
            }
        }

        for (int i = 0; i < C.length; i++) {
            for (int j = 0; j < C.length; j++) {
                if(i == j){
                    new_matrix[i + C.length][j] = -1 * M;
                }else{
                    new_matrix[i + C.length][j] = C[i][j];
                }
            }
        }
//        print1(new_matrix);
        return new_matrix;
    }

    // 将矩阵转置
    public static double[][] reverse(double temp [][]) {
        double[][] new_matrix = new double[temp.length][temp.length];
        for (int i = 0; i < temp.length; i++) {
            for (int j = i; j < temp[i].length; j++) {
                new_matrix[i][j] = temp[j][i];
                new_matrix[j][i] = temp[i][j];
            }
        }
        return new_matrix;
    }

    public static void print1(double temp [][]) {
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[i].length; j++) {
                System.out.print(temp[i][j] + "\t") ;
            }
            System.out.println() ;
        }
    }

    public static int[] TransformTour(int[] tour){
        int[] real_tour = new int[(tour.length - 1) / 2 + 1];
        for(int i = 0; i < real_tour.length - 1; i++){
            real_tour[i] = tour[i * 2];
        }
        real_tour[real_tour.length - 1] = tour[tour.length - 1];

        return real_tour;
    }
}