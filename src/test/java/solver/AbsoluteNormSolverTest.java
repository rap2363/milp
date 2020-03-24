package solver;

import coefficients.Coefficients;
import math.Vector;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AbsoluteNormSolverTest {
    @Test
    public void testAbsoluteNormForSampleDataSet() {
        /*
         * Let's use the absolute-norm minimization algorithm to find a linear function mx + b that minimizes the L1
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
         * "through" the lower point and ignoring the high residual for the most part. The L1 norm however is going to
         * effectively ignore the outliers in this problem, as any shift of the line up will penalize residuals on the
         * y = 1 line.
         *
         * The data points are: (1, 1), (2, 5), (3, 1), (4, 1), (5, 1), (6, 3), (7, 1)
         *
         * We expect the result (m, b) = (0, 1)
         */

        final AbsoluteNormSolver solver = AbsoluteNormSolver.newBuilder()
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
        assertEquals(1, Coefficients.asDouble(solutionVector.get(1)), 1e-15);
        assertEquals(6, solver.getOptimalValue(), 1e-15);
    }
}
