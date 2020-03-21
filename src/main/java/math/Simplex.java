package math;

import coefficients.Coefficient;
import coefficients.Coefficients;
import coefficients.ConstantCoefficient;
import lang.Preconditions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Simplex represents an instance of the following type of LP:
 * max c^x
 * s.t.
 * Ax <= b, Cx = d, Ex >= f,
 * where:
 * x,b,d,f >= 0
 * <p>
 * We use the Big-M method to solve this problem generally, and exit early if the problem is unbounded or infeasible.
 * <p>
 * For the Ax <= b constraints, we add slack variables s_i for each constraint.
 * For the Cx = d constraints, we add artificial variables a_i for each constraint.
 * For the Ex <= f constraints, we subtract slack variables s_i and add a_i artificial variables for each constraint.
 * <p>
 * Using the Big-M method, we alter the original LP to maximize:
 * c^x - M * sum[a_i], which effectively minimizes a_i to 0 wherever possible. If we cannot remove an a_i from the basis
 * we know the problem is infeasible.
 * <p>
 * The Simplex contains a Tableau, which is just a list of vectors we perform pivot operations on to obtain a final
 * solution. The number of rows is equal to the number of constraints, which is also the number of basis variables
 * we use to find the final solution.
 */
public final class Simplex {
    private final Vector costVector;
    private final List<Vector> lessThanInequalities;
    private final List<ConstantCoefficient> lessThanConstant;
    private final int numBasisVariables;
    private final int numSlackVariables;
    private final int[] basisVariables;
    private final Vector solution;

    private Simplex(final Vector costVector,
                    final List<Vector> lessThanInequalities,
                    final List<ConstantCoefficient> lessThanConstant) {
        this.costVector = costVector;
        this.lessThanInequalities = lessThanInequalities;
        this.lessThanConstant = lessThanConstant;
        numBasisVariables = lessThanInequalities.size();
        numSlackVariables = lessThanInequalities.size();
        basisVariables = new int[numBasisVariables];
        this.solution = calculateSolution();
    }

    private Vector calculateSolution() {
        for (int i = 0; i < numBasisVariables; i++) {
            // Initialize with the slack variables
            basisVariables[i] = i + costVector.length();
        }

        final Vector[] tableauVectors = new Vector[numBasisVariables];
        for (int i = 0; i < numBasisVariables; i++) {
            final Vector.Builder slackVariableVectorBuilder = Vector.newBuilder();
            for (int j = 0; j < numSlackVariables; j++) {
                slackVariableVectorBuilder.addCoefficient(i == j ? Coefficients.ONE : Coefficients.ZERO);
            }

            final Vector vectorToAdd = Vector.newBuilder()
                    .addCoefficient(lessThanConstant.get(i))
                    .addFromVector(lessThanInequalities.get(i))
                    .addFromVector(slackVariableVectorBuilder.build())
                    .build();
            tableauVectors[i] = vectorToAdd;
        }

        Tableau tableau = new Tableau(costVector.length() + numSlackVariables, tableauVectors);
        while (true) {
            final Vector objectiveVector = getObjectiveVector(tableau);
            final int pivotCol = tableau.findOptimalPivotCol(objectiveVector);
            if (pivotCol == -1) {
                // Solved
                break;
            }
            final int pivotRow = tableau.findOptimalPivotRow(pivotCol);

            if (pivotRow == -1) {
                // Problem is unbounded above
                break;
            }

            // Update basis vector
            basisVariables[pivotRow] = pivotCol - 1;

            tableau = tableau.pivot(pivotRow, pivotCol);
        }

        // Assemble a solution using the basis variables
        final Coefficient[] solutionCoefficients = new Coefficient[costVector.length()];
        Arrays.fill(solutionCoefficients, Coefficients.ZERO);
        for (int rowIndex = 0; rowIndex < basisVariables.length; rowIndex++) {
            final int basisIndex = basisVariables[rowIndex];
            if (basisIndex < solutionCoefficients.length) {
                solutionCoefficients[basisIndex] = tableau.get(rowIndex, 0);
            }
        }

        final Vector solutionVector = Vector.newBuilder()
                .addAllCoefficients(solutionCoefficients)
                .build();
        System.out.println("Optimal Solution: " + solutionVector);
        System.out.println("Optimal Value: " + solutionVector.dotProduct(costVector));
        return solutionVector;
    }

    public Vector getObjectiveVector(final Tableau tableau) {
        final Vector.Builder objectiveVectorBuilder = Vector.newBuilder();
        for (int col = 0; col < tableau.getWidth(); col++) {
            final Coefficient variableCost = getVariableCost(col);
            Coefficient coefficient = Coefficients.subtract(Coefficients.ZERO, variableCost);
            for (int row = 0; row < tableau.getHeight(); row++) {
                final Coefficient basisCost = getBasisCost(row);
                coefficient = Coefficients.add(
                        coefficient,
                        Coefficients.scaleBy(tableau.get(row, col + 1), basisCost)
                );
            }
            objectiveVectorBuilder.addCoefficient(coefficient);
        }

        return objectiveVectorBuilder.build();
    }

    private Coefficient getVariableCost(final int variableIndex) {
        if (variableIndex >= costVector.length()) {
            // TODO: Return -M for Artificial Variables!
            return Coefficients.ZERO;
        }

        return costVector.get(variableIndex);
    }

    private Coefficient getBasisCost(final int row) {
        return getVariableCost(basisVariables[row]);
    }

    public Vector getSolution() {
        return solution;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private Vector costVector;
        private final List<Vector> lessThanInequalities;
        private final List<ConstantCoefficient> lessThanConstants;

        private Builder() {
            this.lessThanInequalities = new ArrayList<>();
            this.lessThanConstants = new ArrayList<>();
        }

        public Builder withCostVector(final Vector costVector) {
            this.costVector = costVector;
            return this;
        }

        public Builder addLessThanInequality(final Vector lessThanInequality,
                                             final ConstantCoefficient coefficient) {
            Preconditions.checkArgument(
                    Coefficients.isNonNegative(coefficient),
                    "Right Hand Side of inequality must be non-negative!");
            this.lessThanInequalities.add(lessThanInequality);
            this.lessThanConstants.add(coefficient);
            return this;
        }

        public Simplex build() {
            Preconditions.checkNotNull(costVector, "Must provide a cost vector");
            Preconditions.checkArgument(lessThanInequalities.size() > 0, "Problem is unbounded");
            return new Simplex(costVector, lessThanInequalities, lessThanConstants);
        }
    }
}
