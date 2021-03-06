package core;

import coefficients.Coefficient;
import coefficients.Coefficients;
import math.Vector;

import java.util.Arrays;

public final class Tableau {
    private final Vector[] vectors;
    private final int numVariables;

    public Tableau(final int numVariables,
                   final Vector... vectors) {
        this.vectors = vectors;
        this.numVariables = numVariables;
    }

    public int getWidth() {
        return numVariables;
    }

    public int getHeight() {
        return vectors.length;
    }

    /**
     * Create a new Tableau by pivoting on a row and column.
     */
    public Tableau pivot(final int row, final int col) {
        final Vector[] newVectors = new Vector[vectors.length];
        final Coefficient inversePivotCoefficient = Coefficients.invert(
                vectors[row].get(col)
        );
        final Vector inverseScaledVector = vectors[row].scale(inversePivotCoefficient);
        for (int i = 0; i < vectors.length; i++) {
            if (i == row) {
                newVectors[i] = inverseScaledVector;
            } else {
                final Vector vectorToSubtract = inverseScaledVector.scale(vectors[i].get(col));
                newVectors[i] = vectors[i].subtract(vectorToSubtract);
            }
        }

        return new Tableau(numVariables, newVectors);
    }

    public Tableau pivot() {
        final int optimalPivotCol = findOptimalPivotCol();

        if (optimalPivotCol == -1) {
            return this;
        }

        return pivot(optimalPivotCol);
    }

    public Tableau pivot(final int col) {
        return pivot(findOptimalPivotRow(col), col);
    }

    /**
     * Return the "most negative" column, or if all values are non-negative return -1.
     */
    public int findOptimalPivotCol() {
        return findOptimalPivotCol(vectors[vectors.length - 1]);
    }

    /**
     * Return the "most negative" column, or if all values are non-negative return -1.
     */
    public int findOptimalPivotCol(final Vector objectiveRow) {
        int optimalPivotCol = -1;
        Coefficient mostNegativeCoefficient = Coefficients.from(0);

        for (int col = 0; col < numVariables; col++) {
            final Coefficient coefficientToCompare = objectiveRow.get(col);
            if (Coefficients.compare(coefficientToCompare, mostNegativeCoefficient) < 0) {
                optimalPivotCol = col + 1; // The first value is the value of the basis variable
                mostNegativeCoefficient = coefficientToCompare;
            }
        }

        return optimalPivotCol;
    }

    /**
     * Returns the argmin b_i / a_ik, for i in rows, k == col, and ignoring the final row.
     */
    public int findOptimalPivotRow(final int col) {
        int optimalRow = -1;
        Coefficient minimumRatio = Coefficients.from(Double.POSITIVE_INFINITY);
        for (int row = 0; row < vectors.length; row++) {
            final Vector rowVector = vectors[row];
            final Coefficient divisorCoefficient = rowVector.get(col);
            if (!Coefficients.isPositive(divisorCoefficient)) {
                continue;
            }

            final Coefficient ratio = Coefficients.divide(
                    rowVector.get(0),
                    divisorCoefficient
            );

            if (Coefficients.compare(ratio, minimumRatio) < 0) {
                minimumRatio = ratio;
                optimalRow = row;
            }
        }

        return optimalRow;
    }

    public Coefficient get(final int row, final int col) {
        return vectors[row].get(col);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Tableau otherTableau = (Tableau) o;
        return Arrays.equals(vectors, otherTableau.vectors);
    }
}
