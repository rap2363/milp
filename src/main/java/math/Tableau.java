package math;

import coefficients.Coefficient;
import coefficients.Coefficients;

import java.util.Arrays;

public final class Tableau {
    private final Vector[] vectors;

    public Tableau(final Vector... vectors) {
        this.vectors = vectors;
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

        return new Tableau(newVectors);
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
    private int findOptimalPivotCol() {
        final Vector objectiveRow = vectors[vectors.length - 1];
        int optimalPivotCol = -1;
        Coefficient mostNegativeCoefficient = Coefficients.from(0);

        for (int col = 1; col < objectiveRow.length(); col++) {
            final Coefficient coefficientToCompare = objectiveRow.get(col);
            if (Coefficients.compare(coefficientToCompare, mostNegativeCoefficient) < 0) {
                optimalPivotCol = col;
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
        for (int row = 0; row < vectors.length - 1; row++) {
            final Vector rowVector = vectors[row];
            final Coefficient divisorCoefficient = rowVector.get(col);
            if (!Coefficients.isPositive(divisorCoefficient)) {
                continue;
            }

            final Coefficient ratio = Coefficients.divide(
                    rowVector.get(0),
                    rowVector.get(col)
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
