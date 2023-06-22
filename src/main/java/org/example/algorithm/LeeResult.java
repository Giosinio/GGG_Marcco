package org.example.algorithm;

import org.example.Pair;

public class LeeResult {
    public int[][] distanceMatrix;
    public Pair[][] positionsMatrix;

    public LeeResult(int[][] distanceMatrix, Pair[][] positionsMatrix) {
        this.distanceMatrix = distanceMatrix;
        this.positionsMatrix = positionsMatrix;
    }
}
