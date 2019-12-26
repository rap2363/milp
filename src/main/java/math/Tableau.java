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
        final Coefficient inversePivotCoefficient = vectors[row].get(col).inverse();
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

    public Tableau pivot(final int col) {
        return pivot(findOptimalPivotRow(col), col);
    }

    /**
     * Returns the argmin b_i / a_ik, for i in rows, k == col.
     */
    public int findOptimalPivotRow(final int col) {
        int optimalRow = -1;
        Coefficient minimumRatio = Coefficients.from(Double.POSITIVE_INFINITY);
        for (int row = 0; row < vectors.length; row++) {
            final Vector rowVector = vectors[row];
            final Coefficient ratio = Coefficients.divide(rowVector.get(0), rowVector.get(col));

            if (ratio.compareTo(minimumRatio) < 0) {
                minimumRatio = ratio;
                optimalRow = row;
            }
        }

        return optimalRow;
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
