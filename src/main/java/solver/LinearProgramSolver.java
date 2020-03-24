package solver;

import coefficients.ConstantCoefficient;
import core.Simplex;
import lang.Preconditions;
import math.Vector;

import java.util.List;
import java.util.Optional;

/**
 * This solver handles problems of the following type:
 * <p>
 * min [or max] c^x
 * <p>
 * s.t.
 * <p>
 * Ax <= b
 * Cx = d
 * Ef = g
 * x >= 0
 * <p>
 * where x is a vector in R^n.
 * <p>
 * No other requirements need to be made. This class effectively mirrors the Simplex object to solve and contain its
 * final result.
 */
public class LinearProgramSolver implements Solver {
    private final Simplex simplex;

    private LinearProgramSolver(final Simplex simplex) {
        this.simplex = simplex;
    }

    @Override
    public Optional<Vector> getOptimalSolutionIfFeasible() {
        if (!simplex.isFeasible() || !simplex.isBounded()) {
            return Optional.empty();
        }

        return Optional.of(simplex.getOptimalSolution());
    }

    @Override
    public double getOptimalValue() {
        return simplex.getOptimalValue();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private final Simplex.Builder simplexBuilder;

        private Builder() {
            this.simplexBuilder = Simplex.newBuilder();
        }

        public Builder maximizingCost() {
            simplexBuilder.maximizeCostFunction();
            return this;
        }

        public Builder minimizingCost() {
            simplexBuilder.minimizeCostFunction();
            return this;
        }

        public Builder withCostVector(final Vector costVector) {
            simplexBuilder.withCostVector(costVector);
            return this;
        }

        public Builder addLessThanInequality(final ConstantCoefficient lessThanConstant,
                                             final double... lessThanInequalityVariables) {
            simplexBuilder.addLessThanInequality(
                    Vector.newBuilder().addAllDoubleCoefficients(lessThanInequalityVariables).build(),
                    lessThanConstant
            );
            return this;
        }

        public Builder withLessThanInequalities(final List<Vector> lessThanInequalities,
                                                final List<ConstantCoefficient> lessThanConstants) {
            Preconditions.checkArgument(lessThanInequalities.size() == lessThanConstants.size());
            for (int i = 0; i < lessThanConstants.size(); i++) {
                simplexBuilder.addLessThanInequality(lessThanInequalities.get(i), lessThanConstants.get(i));
            }

            return this;
        }

        public Builder addEquality(final ConstantCoefficient equalityConstant,
                                   final double... equalityVariables) {
            simplexBuilder.addEquality(
                    Vector.newBuilder().addAllDoubleCoefficients(equalityVariables).build(),
                    equalityConstant
            );
            return this;
        }

        public Builder withEqualities(final List<Vector> equalities,
                                      final List<ConstantCoefficient> equalityConstants) {
            Preconditions.checkArgument(equalities.size() == equalityConstants.size());
            for (int i = 0; i < equalityConstants.size(); i++) {
                simplexBuilder.addEquality(equalities.get(i), equalityConstants.get(i));
            }

            return this;
        }

        public Builder addGreaterThanInequality(final ConstantCoefficient greaterThanConstant,
                                                final double... greaterThanInequalityVariables) {
            simplexBuilder.addGreaterThanInequality(
                    Vector.newBuilder().addAllDoubleCoefficients(greaterThanInequalityVariables).build(),
                    greaterThanConstant
            );
            return this;
        }

        public Builder addGreaterThanInequality(final ConstantCoefficient greaterThanConstant,
                                                final ConstantCoefficient... greaterThanInequalityVariables) {
            simplexBuilder.addGreaterThanInequality(
                    Vector.newBuilder().addAllCoefficients(greaterThanInequalityVariables).build(),
                    greaterThanConstant
            );
            return this;
        }

        public Builder withGreaterThanInequalities(final List<Vector> greaterThanInequalities,
                                                   final List<ConstantCoefficient> greaterThanConstants) {
            Preconditions.checkArgument(greaterThanInequalities.size() == greaterThanConstants.size());
            for (int i = 0; i < greaterThanConstants.size(); i++) {
                simplexBuilder.addGreaterThanInequality(greaterThanInequalities.get(i), greaterThanConstants.get(i));
            }

            return this;
        }

        public LinearProgramSolver build() {
            return new LinearProgramSolver(simplexBuilder.build());
        }
    }
}
