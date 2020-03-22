package math;

import coefficients.Coefficients;
import core.Simplex;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimplexTest {

    @Test
    public void testSimplexMaximization() {
        assertEquals(
                "[3,12]",
                Simplex.newBuilder()
                        .withCostVector(Vector.newBuilder()
                                .addCoefficient(3)
                                .addCoefficient(2)
                                .build())
                        .addLessThanInequality(Vector.newBuilder()
                                        .addCoefficient(2)
                                        .addCoefficient(1)
                                        .build(),
                                Coefficients.from(18))
                        .addLessThanInequality(Vector.newBuilder()
                                        .addCoefficient(2)
                                        .addCoefficient(3)
                                        .build(),
                                Coefficients.from(42))
                        .addLessThanInequality(Vector.newBuilder()
                                        .addCoefficient(3)
                                        .addCoefficient(1)
                                        .build(),
                                Coefficients.from(24))
                        .build()
                        .getOptimalSolution()
                        .toString()
        );

        assertEquals(
                "[0,3/2]",
                Simplex.newBuilder()
                        .withCostVector(Vector.newBuilder()
                                .addCoefficient(4)
                                .addCoefficient(3)
                                .build())
                        .addLessThanInequality(Vector.newBuilder()
                                        .addCoefficient(2)
                                        .addCoefficient(3)
                                        .build(),
                                Coefficients.from(6))
                        .addLessThanInequality(Vector.newBuilder()
                                        .addCoefficient(3)
                                        .addCoefficient(2)
                                        .build(),
                                Coefficients.from(3))
                        .addLessThanInequality(Vector.newBuilder()
                                        .addCoefficient(0)
                                        .addCoefficient(2)
                                        .build(),
                                Coefficients.from(5))
                        .addLessThanInequality(Vector.newBuilder()
                                        .addCoefficient(2)
                                        .addCoefficient(1)
                                        .build(),
                                Coefficients.from(4))
                        .build()
                        .getOptimalSolution()
                        .toString());

        assertEquals(
                "[2,0,1]",
                Simplex.newBuilder()
                        .withCostVector(Vector.newBuilder()
                                .addCoefficient(5)
                                .addCoefficient(4)
                                .addCoefficient(3)
                                .build())
                        .addLessThanInequality(Vector.newBuilder()
                                        .addCoefficient(2)
                                        .addCoefficient(3)
                                        .addCoefficient(1)
                                        .build(),
                                Coefficients.from(5))
                        .addLessThanInequality(Vector.newBuilder()
                                        .addCoefficient(4)
                                        .addCoefficient(1)
                                        .addCoefficient(2)
                                        .build(),
                                Coefficients.from(11))
                        .addLessThanInequality(Vector.newBuilder()
                                        .addCoefficient(3)
                                        .addCoefficient(4)
                                        .addCoefficient(2)
                                        .build(),
                                Coefficients.from(8))
                        .build()
                        .getOptimalSolution()
                        .toString());
    }

    @Test
    public void testSimplexMaximizationWithEqualityConstraint() {
        assertEquals(
                "[10,0]",
                Simplex.newBuilder()
                        .withCostVector(Vector.newBuilder()
                                .addCoefficient(2)
                                .addCoefficient(1)
                                .build())
                        .addEquality(Vector.newBuilder()
                                        .addCoefficient(1)
                                        .addCoefficient(1)
                                        .build(),
                                Coefficients.from(10))
                        .build()
                        .getOptimalSolution()
                        .toString()
        );

        final Simplex sameSimplexWithLessThanAndGreaterThanInsteadOfEquality = Simplex.newBuilder()
                .withCostVector(Vector.newBuilder()
                        .addCoefficient(2)
                        .addCoefficient(1)
                        .build())
                .addGreaterThanInequality(Vector.newBuilder()
                                .addCoefficient(1)
                                .addCoefficient(1)
                                .build(),
                        Coefficients.from(10))
                .addLessThanInequality(Vector.newBuilder()
                                .addCoefficient(1)
                                .addCoefficient(1)
                                .build(),
                        Coefficients.from(10))
                .build();

        assertEquals(20.0, sameSimplexWithLessThanAndGreaterThanInsteadOfEquality.getOptimalValue(), 0d);
    }

    @Test
    public void testSimplexMaximizationWithEqualityAndInequalityConstraint() {
        assertEquals(
                "[5,15]",
                Simplex.newBuilder()
                        .withCostVector(Vector.newBuilder()
                                .addCoefficient(2)
                                .addCoefficient(1)
                                .build())
                        .addLessThanInequality(Vector.newBuilder()
                                        .addCoefficient(1)
                                        .addCoefficient(1)
                                        .build(),
                                Coefficients.from(20))
                        .addEquality(Vector.newBuilder()
                                        .addCoefficient(3)
                                        .addCoefficient(-1)
                                        .build(),
                                Coefficients.from(0))
                        .build()
                        .getOptimalSolution()
                        .toString()
        );

        assertEquals(
                "[5/4,15/4]",
                Simplex.newBuilder()
                        .withCostVector(Vector.newBuilder()
                                .addCoefficient(2)
                                .addCoefficient(1)
                                .build())
                        .addLessThanInequality(Vector.newBuilder()
                                        .addCoefficient(1)
                                        .addCoefficient(1)
                                        .build(),
                                Coefficients.from(20))
                        .addEquality(Vector.newBuilder()
                                        .addCoefficient(3)
                                        .addCoefficient(-1)
                                        .build(),
                                Coefficients.from(0))
                        .addEquality(Vector.newBuilder()
                                        .addCoefficient(1)
                                        .addCoefficient(1)
                                        .build(),
                                Coefficients.from(5))
                        .build()
                        .getOptimalSolution()
                        .toString()
        );
    }

    @Test
    public void testInfeasibleSolution() {
        final Simplex simplex = Simplex.newBuilder()
                .withCostVector(Vector.newBuilder()
                        .addCoefficient(2)
                        .addCoefficient(1)
                        .build())
                .addLessThanInequality(Vector.newBuilder()
                                .addCoefficient(1)
                                .addCoefficient(1)
                                .build(),
                        Coefficients.from(20))
                .addEquality(Vector.newBuilder()
                                .addCoefficient(3)
                                .addCoefficient(-1)
                                .build(),
                        Coefficients.from(0))
                .addEquality(Vector.newBuilder()
                                .addCoefficient(1)
                                .addCoefficient(1)
                                .build(),
                        Coefficients.from(5))
                .addEquality(Vector.newBuilder()
                                .addCoefficient(1)
                                .addCoefficient(1)
                                .build(),
                        Coefficients.from(6))
                .build();
        assertEquals(
                "[5/4,15/4]",
                simplex.getOptimalSolution().toString()
        );

        assertEquals(Double.NEGATIVE_INFINITY, simplex.getOptimalValue(), 0d);
        assertFalse(simplex.isFeasible());
        assertTrue(simplex.isBounded());
    }

    @Test
    public void testUnboundedAbove() {
        final Simplex simplex = Simplex.newBuilder()
                .withCostVector(Vector.newBuilder()
                        .addCoefficient(1)
                        .addCoefficient(1)
                        .build())
                .addGreaterThanInequality(Vector.newBuilder()
                                .addCoefficient(1)
                                .addCoefficient(1)
                                .build(),
                        Coefficients.from(20))
                .build();
        assertEquals(
                "[20,0]",
                simplex.getOptimalSolution().toString()
        );

        assertEquals(Double.POSITIVE_INFINITY, simplex.getOptimalValue(), 0d);
        assertTrue(simplex.isFeasible());
        assertFalse(simplex.isBounded());
    }

    @Test
    public void testMinimizationWithGreaterThanInequality() {
        assertEquals(
                "[0,1]",
                Simplex.newBuilder()
                        .minimizeCostFunction()
                        .withCostVector(Vector.newBuilder()
                                .addCoefficient(2)
                                .addCoefficient(1)
                                .build())
                        .addGreaterThanInequality(Vector.newBuilder()
                                        .addCoefficient(1)
                                        .addCoefficient(1)
                                        .build(),
                                Coefficients.from(1))
                        .addGreaterThanInequality(Vector.newBuilder()
                                        .addCoefficient(1)
                                        .addCoefficient(1)
                                        .build(),
                                Coefficients.from(-1))
                        .build()
                        .getOptimalSolution()
                        .toString()
        );
    }


    @Test
    public void testMaximizationWithLessThanAndGreaterThanInequality() {
        assertEquals(
                "[2,0]",
                Simplex.newBuilder()
                        .withCostVector(Vector.newBuilder()
                                .addCoefficient(2)
                                .addCoefficient(1)
                                .build())
                        .addGreaterThanInequality(Vector.newBuilder()
                                        .addCoefficient(1)
                                        .addCoefficient(1)
                                        .build(),
                                Coefficients.from(1))
                        .addLessThanInequality(Vector.newBuilder()
                                        .addCoefficient(1)
                                        .addCoefficient(1)
                                        .build(),
                                Coefficients.from(2))
                        .build()
                        .getOptimalSolution()
                        .toString()
        );
    }

    @Test
    public void testMaximizationWithAllThreeTypesOfInequalities() {
        assertEquals(
                "[2,0,2]",
                Simplex.newBuilder()
                        .withCostVector(Vector.newBuilder()
                                .addCoefficient(2)
                                .addCoefficient(-1)
                                .addCoefficient(3)
                                .build())
                        .addEquality(Vector.newBuilder()
                                        .addCoefficient(1)
                                        .addCoefficient(1)
                                        .addCoefficient(1)
                                        .build(),
                                Coefficients.from(4))
                        .addGreaterThanInequality(Vector.newBuilder()
                                        .addCoefficient(1)
                                        .addCoefficient(-2)
                                        .addCoefficient(1)
                                        .build(),
                                Coefficients.from(3))
                        .addLessThanInequality(Vector.newBuilder()
                                        .addCoefficient(0)
                                        .addCoefficient(2)
                                        .addCoefficient(1)
                                        .build(),
                                Coefficients.from(2))
                        .build()
                        .getOptimalSolution()
                        .toString()
        );
    }

    @Test
    public void testDietProblem() {
        // http://www.cs.toronto.edu/~anikolov/CSC473W19/MG-LP.pdf
        final Simplex dietProblemSimplex = Simplex.newBuilder()
                .minimizeCostFunction()
                .withCostVector(Vector.newBuilder()
                        .addRationalCoefficient(3, 4)
                        .addRationalCoefficient(1, 2)
                        .addRationalCoefficient(15, 100)
                        .build())
                .addGreaterThanInequality(Vector.newBuilder()
                                .addCoefficient(35)
                                .addRationalCoefficient(1, 2)
                                .addRationalCoefficient(1, 2)
                                .build(),
                        Coefficients.from(1, 2))
                .addGreaterThanInequality(Vector.newBuilder()
                                .addCoefficient(60)
                                .addCoefficient(300)
                                .addCoefficient(10)
                                .build(),
                        Coefficients.from(15))
                .addGreaterThanInequality(Vector.newBuilder()
                                .addCoefficient(30)
                                .addCoefficient(20)
                                .addCoefficient(10)
                                .build(),
                        Coefficients.from(4))
                .build();

        assertEquals(
                0.07,
                dietProblemSimplex.getOptimalValue(), 1e-3
        );

        assertTrue(dietProblemSimplex.isBounded());
        assertTrue(dietProblemSimplex.isFeasible());
    }
}
