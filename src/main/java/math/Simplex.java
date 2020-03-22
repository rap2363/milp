package math;

import coefficients.Coefficient;
import coefficients.Coefficients;
import coefficients.ConstantCoefficient;
import coefficients.LinearMCoefficient;
import lang.Preconditions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

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
    private final List<ConstantCoefficient> lessThanConstants;
    private final List<Vector> equalities;
    private final List<ConstantCoefficient> equalityConstants;
    private final List<Vector> greaterThanInequalities;
    private final List<ConstantCoefficient> greaterThanConstants;
    private final int numBasisVariables;
    private final int numSlackVariables;
    private final int numArtificialVariables;
    private final int[] basisVariables;
    private final SimplexResults simplexResults;

    private Simplex(final Vector costVector,
                    final List<Vector> lessThanInequalities,
                    final List<ConstantCoefficient> lessThanConstants,
                    final List<Vector> equalities,
                    final List<ConstantCoefficient> equalityConstants,
                    final List<Vector> greaterThanInequalities,
                    final List<ConstantCoefficient> greaterThanConstants) {
        this.costVector = costVector;
        this.lessThanInequalities = lessThanInequalities;
        this.lessThanConstants = lessThanConstants;
        this.equalities = equalities;
        this.equalityConstants = equalityConstants;
        this.greaterThanInequalities = greaterThanInequalities;
        this.greaterThanConstants = greaterThanConstants;
        numSlackVariables = lessThanInequalities.size() + greaterThanInequalities.size();
        numArtificialVariables = equalities.size() + greaterThanInequalities.size();
        numBasisVariables = lessThanInequalities.size() + equalities.size() + greaterThanInequalities.size();
        basisVariables = new int[numBasisVariables];
        this.simplexResults = calculateSolution();
    }

    private SimplexResults calculateSolution() {
        // Initialize the basis with the slack and artificial variables
        final int slackVariableOffset = costVector.length();
        for (int i = 0; i < lessThanInequalities.size(); i++) {
            basisVariables[i] = slackVariableOffset + i;
        }
        final int equalityVariableOffset = slackVariableOffset + lessThanInequalities.size();
        for (int i = 0; i < equalities.size(); i++) {
            basisVariables[i + lessThanInequalities.size()] = equalityVariableOffset + i;
        }
        final int greaterThanVariableOffset
                = equalityVariableOffset + equalities.size() + greaterThanInequalities.size();
        for (int i = 0; i < greaterThanInequalities.size(); i++) {
            basisVariables[i + lessThanInequalities.size() + equalities.size()] = greaterThanVariableOffset + i;
        }

        final List<Vector> tableauVectors = new ArrayList<>(numBasisVariables);

        // Add less than inequality variable rows
        for (int i = 0; i < lessThanConstants.size(); i++) {
            final Vector.Builder lessThanInequalityVectorBuilder = Vector.newBuilder();
            for (int j = 0; j < numSlackVariables + numArtificialVariables; j++) {
                lessThanInequalityVectorBuilder.addCoefficient(i == j ? Coefficients.ONE : Coefficients.ZERO);
            }

            final Vector vectorToAdd = Vector.newBuilder()
                    .addCoefficient(lessThanConstants.get(i))
                    .addFromVector(lessThanInequalities.get(i))
                    .addFromVector(lessThanInequalityVectorBuilder.build())
                    .build();
            tableauVectors.add(vectorToAdd);
        }

        // Add equality rows
        for (int i = 0; i < equalities.size(); i++) {
            final Vector.Builder equalityVectorBuilder = Vector.newBuilder();
            for (int j = 0; j < numSlackVariables + numArtificialVariables; j++) {
                final int artficialOffset = i + numSlackVariables;
                equalityVectorBuilder.addCoefficient(artficialOffset == j ? Coefficients.ONE : Coefficients.ZERO);
            }

            final Vector vectorToAdd = Vector.newBuilder()
                    .addCoefficient(equalityConstants.get(i))
                    .addFromVector(equalities.get(i))
                    .addFromVector(equalityVectorBuilder.build())
                    .build();
            tableauVectors.add(vectorToAdd);
        }

        // Add the greater than inequality variable rows
        for (int i = 0; i < greaterThanInequalities.size(); i++) {
            final Vector.Builder greaterThanInequalityVectorBuilder = Vector.newBuilder();
            for (int j = 0; j < numSlackVariables + numArtificialVariables; j++) {
                final int lessThanOffset = i + lessThanInequalities.size();
                final int artficialOffset = i + numSlackVariables;
                // Subtract slack variables
                if (j == i + lessThanOffset) {
                    greaterThanInequalityVectorBuilder.addCoefficient(Coefficients.NEGATIVE_ONE);
                } else if (j == i + artficialOffset) {
                    greaterThanInequalityVectorBuilder.addCoefficient(Coefficients.ONE);
                } else {
                    greaterThanInequalityVectorBuilder.addCoefficient(Coefficients.ZERO);
                }
            }

            final Vector vectorToAdd = Vector.newBuilder()
                    .addCoefficient(greaterThanConstants.get(i))
                    .addFromVector(greaterThanInequalities.get(i))
                    .addFromVector(greaterThanInequalityVectorBuilder.build())
                    .build();
            tableauVectors.add(vectorToAdd);
        }

        Tableau tableau = new Tableau(
                costVector.length() + numSlackVariables + numArtificialVariables,
                tableauVectors.toArray(new Vector[0])
        );

        boolean isBounded = true;
        final List<Vector> simplexSolutions = new ArrayList<>();
        while (true) {
            final Vector currentSolution = createSolutionFromBasis(tableau, costVector.length(), basisVariables);
            simplexSolutions.add(currentSolution);

            final Vector objectiveVector = getObjectiveVector(tableau);
            final int pivotCol = tableau.findOptimalPivotCol(objectiveVector);
            if (pivotCol == -1) {
                // Solved
                break;
            }

            final int pivotRow = tableau.findOptimalPivotRow(pivotCol);

            if (pivotRow == -1) {
                // Problem is unbounded above
                isBounded = false;
                break;
            }

            // Update basis vector
            basisVariables[pivotRow] = pivotCol - 1;

            tableau = tableau.pivot(pivotRow, pivotCol);
        }

        // Assemble a solution using the basis variables
        final Vector solutionVector = createSolutionFromBasis(tableau, costVector.length(), basisVariables);

        final boolean isFeasible = isFeasibleSolution(solutionVector);
        final double optimalValue;
        if (isFeasible && isBounded) {
            optimalValue = solutionVector.dotProductAsDouble(costVector);
        } else {
            optimalValue = isFeasible ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        }

        return new SimplexResults(simplexSolutions, isFeasible, isBounded);
    }

    private static Vector createSolutionFromBasis(final Tableau tableau,
                                                  final int numRealVariables,
                                                  final int[] basisVariables) {
        final Coefficient[] solutionCoefficients = new Coefficient[numRealVariables];
        Arrays.fill(solutionCoefficients, Coefficients.ZERO);
        for (int rowIndex = 0; rowIndex < basisVariables.length; rowIndex++) {
            final int basisIndex = basisVariables[rowIndex];
            if (basisIndex < solutionCoefficients.length) {
                solutionCoefficients[basisIndex] = tableau.get(rowIndex, 0);
            }
        }

        return Vector.newBuilder().addAllCoefficients(solutionCoefficients).build();
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
        if (variableIndex < costVector.length()) {
            // Regular variables
            return costVector.get(variableIndex);
        } else if (variableIndex < costVector.length() + numSlackVariables) {
            // Slack variables
            return Coefficients.ZERO;
        } else {
            // Artificial variables (-M)
            return LinearMCoefficient.fromSlopeValue(-1);
        }
    }

    private Coefficient getBasisCost(final int row) {
        return getVariableCost(basisVariables[row]);
    }

    private boolean isFeasibleSolution(final Vector solution) {
        for (int i = 0; i < lessThanInequalities.size(); i++) {
            final Coefficient cost = Vector.dotProduct(lessThanInequalities.get(i), solution);
            if (Coefficients.greaterThan(cost, lessThanConstants.get(i))) {
                return false;
            }
        }

        for (int i = 0; i < equalities.size(); i++) {
            final Coefficient cost = Vector.dotProduct(equalities.get(i), solution);
            if (!Coefficients.equalTo(cost, equalityConstants.get(i))) {
                return false;
            }
        }

        for (int i = 0; i < greaterThanInequalities.size(); i++) {
            final Coefficient cost = Vector.dotProduct(greaterThanInequalities.get(i), solution);
            if (Coefficients.lessThan(cost, greaterThanConstants.get(i))) {
                return false;
            }
        }

        return true;
    }

    public Vector getOptimalSolution() {
        return simplexResults.optimalSolution;
    }

    public List<Vector> getSolutionTrace() {
        return simplexResults.solutionTrace;
    }

    public double getOptimalValue() {
        return simplexResults.optimalValue;
    }

    public boolean isFeasible() {
        return simplexResults.isFeasible;
    }

    public boolean isBounded() {
        return simplexResults.isBounded;
    }

    /**
     * Simple struct to hold the results of the Simplex algorithm.
     */
    private class SimplexResults {
        private final List<Vector> solutionTrace;
        private final Vector optimalSolution;
        private final double optimalValue;
        private final boolean isFeasible;
        private final boolean isBounded;

        public SimplexResults(final List<Vector> solutionTrace,
                              final boolean isFeasible,
                              final boolean isBounded) {
            this.solutionTrace = solutionTrace;
            this.optimalSolution = solutionTrace.get(solutionTrace.size() - 1);
            this.optimalValue = optimalSolution.dotProductAsDouble(costVector);
            this.isFeasible = isFeasible;
            this.isBounded = isBounded;
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private Vector costVector;
        private final List<Vector> lessThanInequalities;
        private final List<ConstantCoefficient> lessThanConstants;
        private final List<Vector> equalities;
        private final List<ConstantCoefficient> equalityConstants;
        private final List<Vector> greaterThanInequalities;
        private final List<ConstantCoefficient> greaterThanConstants;

        private Builder() {
            this.lessThanInequalities = new ArrayList<>();
            this.lessThanConstants = new ArrayList<>();
            this.equalities = new ArrayList<>();
            this.equalityConstants = new ArrayList<>();
            this.greaterThanInequalities = new ArrayList<>();
            this.greaterThanConstants = new ArrayList<>();
        }

        public Builder withCostVector(final Vector costVector) {
            this.costVector = costVector;
            return this;
        }

        public Builder addLessThanInequality(final Vector lessThanInequality,
                                             final ConstantCoefficient lessThanConstant) {
            Preconditions.checkArgument(
                    Coefficients.isNonNegative(lessThanConstant),
                    "Right Hand Side of inequality must be non-negative!");
            this.lessThanInequalities.add(lessThanInequality);
            this.lessThanConstants.add(lessThanConstant);
            return this;
        }

        public Builder addEquality(final Vector equality,
                                   final ConstantCoefficient equalityConstant) {
            Preconditions.checkArgument(
                    Coefficients.isNonNegative(equalityConstant),
                    "Right Hand Side of equality must be non-negative!");
            this.equalities.add(equality);
            this.equalityConstants.add(equalityConstant);
            return this;
        }

        public Builder addGreaterThanInequality(final Vector greaterThan,
                                                final ConstantCoefficient greaterThanConstant) {
            Preconditions.checkArgument(
                    Coefficients.isNonNegative(greaterThanConstant),
                    "Right Hand Side of greaterThan must be non-negative!");
            this.greaterThanInequalities.add(greaterThan);
            this.greaterThanConstants.add(greaterThanConstant);
            return this;
        }

        public Simplex build() {
            Preconditions.checkNotNull(costVector, "Must provide a cost vector");
            Preconditions.checkArgument(lessThanInequalities
                            .stream()
                            .map(Vector::length)
                            .allMatch(Predicate.isEqual(costVector.length())),
                    "One or more less than inequalities provided do not match the length of the cost vector"
            );
            Preconditions.checkArgument(equalities
                            .stream()
                            .map(Vector::length)
                            .allMatch(Predicate.isEqual(costVector.length())),
                    "One or more less than inequalities provided do not match the length of the cost vector"
            );
            Preconditions.checkArgument(greaterThanInequalities
                            .stream()
                            .map(Vector::length)
                            .allMatch(Predicate.isEqual(costVector.length())),
                    "One or more greater than inequalities provided do not match the length of the cost vector"
            );

            return new Simplex(
                    costVector,
                    lessThanInequalities,
                    lessThanConstants,
                    equalities,
                    equalityConstants,
                    greaterThanInequalities,
                    greaterThanConstants
            );
        }
    }
}
