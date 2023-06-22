package org.example.algorithm;

import java.util.LinkedList;
import java.util.Queue;
import org.example.Pair;

public class LeeAlgorithmSolver {
    private static final int[] k_x = {-1, 0, 1, 0};
    private static final int[] k_y = {0, 1, 0, -1};

    private static void convertCharacterMatrixToDistanceMatrix(int[][] distanceMatrix, char[][] characterMatrix, int sizeRows,
                                                               int sizeColumns) {
        for (int i = 0; i < sizeRows; i++) {
            for (int j = 0; j < sizeColumns; j++) {
                if (characterMatrix[i][j] == '#' || characterMatrix[i][j] == 'o') {
                    distanceMatrix[i][j] = -1;
                }
                if (characterMatrix[i][j] == ' ' || characterMatrix[i][j] == 'm' || characterMatrix[i][j] == 's') {
                    distanceMatrix[i][j] = 0;
                }
            }
        }
    }

    /**
     * The classic implementation of Lee's Algorithm
     *
     * @param characterMatrix - the board received from the server, parsed by Istvan
     * @param start_x         - part of the starting point
     * @param start_y         - part of the starting point
     */
    public static LeeResult solveLee(char[][] characterMatrix, int start_x, int start_y) {
        int matrixSizeRows = characterMatrix.length;
        int matrixSizeColumns = characterMatrix[0].length;
        int[][] distanceMatrix = new int[matrixSizeRows][matrixSizeColumns];
        Pair[][] positionsMatrix =
                new Pair[matrixSizeRows][matrixSizeColumns]; // for a certain point, it saves from where we reached this point
        convertCharacterMatrixToDistanceMatrix(distanceMatrix, characterMatrix, matrixSizeRows, matrixSizeColumns);
        distanceMatrix[start_x][start_y] = 0;
        Queue<Pair<Integer, Integer>> coveredPoints = new LinkedList<>();
        coveredPoints.add(new Pair<>(start_x, start_y));
        while (!coveredPoints.isEmpty()) {
            Pair<Integer, Integer> currentPoint = coveredPoints.poll();
            int currentX = currentPoint.getFirst(), currentY = currentPoint.getSecond();
            for (int i = 0; i < 4; i++) {
                int new_x = currentX + k_x[i];
                int new_y = currentY + k_y[i];
                if (distanceMatrix[new_x][new_y] == 0) {
                    distanceMatrix[new_x][new_y] = 1 + distanceMatrix[currentX][currentY];
                    positionsMatrix[new_x][new_y] = new Pair<>(currentX, currentY);
                    coveredPoints.add(new Pair<>(new_x, new_y));
                    if (characterMatrix[currentX][currentY] == ' ') {
                        int new_x_run = new_x + k_x[i];
                        int new_y_run = new_y + k_y[i];
                        if (distanceMatrix[new_x_run][new_y_run] == 0) {
                            distanceMatrix[new_x_run][new_y_run] = 1 + distanceMatrix[currentX][currentY];
                            positionsMatrix[new_x_run][new_y_run] = new Pair<>(currentX, currentY);
                            coveredPoints.add(new Pair<>(new_x_run, new_y_run));
                        }
                    }
                }
            }
        }
        return new LeeResult(distanceMatrix, positionsMatrix);
    }
}
