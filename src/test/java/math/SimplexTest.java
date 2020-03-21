package math;

import coefficients.Coefficients;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
                        .getSolution()
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
                        .getSolution()
                        .toString());
    }
}
