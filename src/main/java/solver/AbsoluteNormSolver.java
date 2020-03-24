package solver;

import coefficients.Coefficient;
import coefficients.Coefficients;
import coefficients.ConstantCoefficient;
import core.Simplex;
import lang.Preconditions;
import math.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This solver handles problems of the following type:
 * <p>
 * min sum(|Ax - y|)  where the absolute value is taken element wise over each element in the vector Ax - y.
 * <p>
 * where x is a vector in R^n.
 * <p>
 * We solve this problem using by transforming the problem into a modified LP like so:
 * <p>
 * min [0^n | 1^m]^z (where z = [x | t] and is a vector in R^n+m)
 * <p>
 * s.t. [A | -Im] z <= y and [A | Im ] >= y (where Im is the m x m identity matrix)
 * <p>
 * The modified LP contains n+m variables and 2m constraints
 */
public final class AbsoluteNormSolver implements Solver {
    private final Vector maybeOptimalSolution;
    private final double optimalValue;

    private AbsoluteNormSolver(final List<Vector> dataVectors,
                               final Vector targetVector) {
        // Build a simplex from the data vectors and target vectors
        final Simplex modifiedSimplex = createModifiedSimplex(dataVectors, targetVector);
        this.maybeOptimalSolution = getSolutionFromModifiedSimplex(modifiedSimplex);
        this.optimalValue = getValueFromModifiedSimplex(modifiedSimplex);
    }

    private static Vector getSolutionFromModifiedSimplex(final Simplex modifiedSimplex) {
        if (!modifiedSimplex.isFeasible()) {
            return null;
        }

        // The solution is whatever we select for the first n values
        final Coefficient[] allValues = modifiedSimplex.getOptimalSolution().getValues();
        return Vector.newBuilder()
                .addAllCoefficients(Arrays.copyOfRange(allValues, 0, allValues.length - 1))
                .build();
    }

    private static double getValueFromModifiedSimplex(final Simplex modifiedSimplex) {
        return modifiedSimplex.isFeasible() ? modifiedSimplex.getOptimalValue() : Double.POSITIVE_INFINITY;
    }

    private static Simplex createModifiedSimplex(final List<Vector> dataVectors, final Vector targetVector) {
        final Simplex.Builder simplexBuilder = Simplex.newBuilder()
                .minimizeCostFunction()
                .withCostVector(Vector.newBuilder()
                        .addAllCoefficients(IntStream.range(0, dataVectors.get(0).length())
                                .mapToObj(ignored -> Coefficients.ZERO)
                                .collect(Collectors.toList()))
                        .addAllCoefficients(IntStream.range(0, targetVector.length())
                                .mapToObj(ignored -> Coefficients.ONE)
                                .collect(Collectors.toList()))
                        .build());

        for (int i = 0; i < targetVector.length(); i++) {
            final ConstantCoefficient targetCoefficient = (ConstantCoefficient) targetVector.get(i);
            final int loopIndex = i;
            // Add each less than inequality
            simplexBuilder.addLessThanInequality(
                    dataVectors.get(i).toBuilder()
                            .addAllCoefficients()
                            .addAllCoefficients(IntStream.range(0, targetVector.length())
                                    .mapToObj(ind -> ind == loopIndex ? Coefficients.NEGATIVE_ONE : Coefficients.ZERO)
                                    .collect(Collectors.toList()))
                            .build(),
                    targetCoefficient
            );

            // Add each greater than inequality
            simplexBuilder.addGreaterThanInequality(
                    dataVectors.get(i).toBuilder()
                            .addAllCoefficients(IntStream.range(0, targetVector.length())
                                    .mapToObj(ind -> ind == loopIndex ? Coefficients.ONE : Coefficients.ZERO)
                                    .collect(Collectors.toList()))
                            .build(),
                    targetCoefficient
            );
        }

        return simplexBuilder.build();
    }

    @Override
    public Optional<Vector> getOptimalSolutionIfFeasible() {
        return Optional.ofNullable(maybeOptimalSolution);
    }

    @Override
    public double getOptimalValue() {
        return optimalValue;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private final List<Vector> dataVectors;
        private Vector targetVector;

        private Builder() {
            this.dataVectors = new ArrayList<>();
        }

        public Builder withTargetVector(final Vector targetVector) {
            this.targetVector = targetVector;
            return this;
        }

        public Builder addDataVector(final Vector dataVector) {
            this.dataVectors.add(dataVector);
            return this;
        }

        public AbsoluteNormSolver build() {
            Preconditions.checkNotNull(targetVector);
            Preconditions.checkArgument(
                    targetVector.length() == dataVectors.size(),
                    "Number of data vectors must equal size of target vector!");
            Preconditions.checkArgument(
                    dataVectors.size() > 0,
                    "Must provide at least one data vector");
            return new AbsoluteNormSolver(dataVectors, targetVector);
        }
    }
}
