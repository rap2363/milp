package coefficients;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CoefficientsTest {
    @Test
    public void testFloor() {
        assertEquals(
            new IntegerCoefficient(4),
            new IntegerCoefficient(4).floor()
        );

        assertEquals(
            new IntegerCoefficient(4),
            new DoubleCoefficient(4.3213).floor()
        );

        assertEquals(
            new IntegerCoefficient(4),
            new RationalCoefficient(17, 4).floor()
        );
    }

    @Test
    public void testCeil() {
        assertEquals(
            new IntegerCoefficient(4),
            new IntegerCoefficient(4).ceil()
        );

        assertEquals(
            new IntegerCoefficient(5),
            new DoubleCoefficient(4.3213).ceil()
        );

        assertEquals(
            new IntegerCoefficient(5),
            new RationalCoefficient(17, 4).ceil()
        );
    }

    @Test
    public void testMultiplyIntAndInt() {
        assertEquals(
            "-12",
            Coefficients.multiply(new IntegerCoefficient(-3), new IntegerCoefficient(4)).toString()
        );
    }

    @Test
    public void testMultiplyIntAndDouble() {
        assertEquals(
            "-13.5",
            Coefficients.multiply(new IntegerCoefficient(-3), new DoubleCoefficient(4.5)).toString()
        );
    }

    @Test
    public void testMultiplyIntAndRational() {
        assertEquals(
            "2",
            Coefficients.multiply(new IntegerCoefficient(-3), new RationalCoefficient(-4, 6)).toString()
        );

        assertEquals(
            "3/4",
            Coefficients.multiply(new IntegerCoefficient(6), new IntegerCoefficient(8).inverse()).toString()
        );

        assertEquals(
            "1",
            Coefficients.multiply(new IntegerCoefficient(6), new IntegerCoefficient(6).inverse()).toString()
        );
    }
}