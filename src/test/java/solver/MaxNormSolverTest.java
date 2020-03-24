package solver;

import coefficients.Coefficients;
import math.Vector;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MaxNormSolverTest {
    @Test
    public void testMaxNormForSampleDataSet() {
        /*
         * Let's use the max-norm minimization algorithm to find a linear function mx + b that minimizes the infinity
         * norm for the following data points:
         *
         * |
         * |
         * |             x
         * |
         * |                                                          x
         * |
         * |    x                 x        x        x        x                 x
         * |____________________________________________________________________________________________________________
         *
         * Normally a least squares regression would yield a slightly negative tending curve that is more or less going
         * "through" the lower point and ignoring the high residual for the most part. The infinity norm however is
         * trying to minimize the maximum residual, so it will strike as a horizontal line through the data set trying
         * to minimize the outlier residual as much as possible.
         *
         * The data points are: (1, 1), (2, 5), (3, 1), (4, 1), (5, 1), (6, 3), (7, 1)
         *
         * We expect the result (m, b) = (0, 3)
         */

        final MaxNormSolver solver = MaxNormSolver.newBuilder()
                .withTargetVector(Vector.newBuilder()
                        .addAllDoubleCoefficients(1, 5, 1, 1, 1, 3, 1)
                        .build())
                .addDataVector(Vector.newBuilder().addAllDoubleCoefficients(1, 1).build())
                .addDataVector(Vector.newBuilder().addAllDoubleCoefficients(2, 1).build())
                .addDataVector(Vector.newBuilder().addAllDoubleCoefficients(3, 1).build())
                .addDataVector(Vector.newBuilder().addAllDoubleCoefficients(4, 1).build())
                .addDataVector(Vector.newBuilder().addAllDoubleCoefficients(5, 1).build())
                .addDataVector(Vector.newBuilder().addAllDoubleCoefficients(6, 1).build())
                .addDataVector(Vector.newBuilder().addAllDoubleCoefficients(7, 1).build())
                .build();

        assertTrue(solver.getOptimalSolutionIfFeasible().isPresent());
        final Vector solutionVector = solver.getOptimalSolutionIfFeasible().get();
        assertEquals(0, Coefficients.asDouble(solutionVector.get(0)), 1e-15);
        assertEquals(3, Coefficients.asDouble(solutionVector.get(1)), 1e-15);
        assertEquals(2, solver.getOptimalValue(), 1e-15);
    }
}
