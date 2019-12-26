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
            "-3/4",
            Coefficients.multiply(new IntegerCoefficient(-6), new IntegerCoefficient(8).inverse()).toString()
        );

        assertEquals(
            "1",
            Coefficients.multiply(new IntegerCoefficient(6), new IntegerCoefficient(6).inverse()).toString()
        );
    }

    @Test
    public void testAddIntAndRational() {
        assertEquals(
            "7/3",
            Coefficients.add(new IntegerCoefficient(3), new RationalCoefficient(-4, 6)).toString()
        );

        assertEquals(
            "-47/8",
            Coefficients.add(new IntegerCoefficient(-6), new IntegerCoefficient(8).inverse()).toString()
        );

        assertEquals(
            "7/6",
            Coefficients.add(new IntegerCoefficient(1), new IntegerCoefficient(6).inverse()).toString()
        );
    }

    @Test
    public void testAddDoubleAndRational() {
        assertEquals(
            2.3333333,
            ((DoubleCoefficient) Coefficients.add(
                new DoubleCoefficient(3), new RationalCoefficient(-4, 6))).getValue(),
            1e-5
        );

        assertEquals(
            -5.875,
            ((DoubleCoefficient) Coefficients.add(
                new DoubleCoefficient(-6), new IntegerCoefficient(8).inverse())).getValue(),
            1e-5
        );

        assertEquals(
            7 / 6.0,
            ((DoubleCoefficient) Coefficients.add(
                new DoubleCoefficient(1), new IntegerCoefficient(6).inverse())).getValue(),
            1e-5
        );
    }

    @Test
    public void testAddRationals() {
        assertEquals(
            "17/20",
            Coefficients.add(
                Coefficients.fromNumeratorAndDenominator(3, 5),
                Coefficients.fromNumeratorAndDenominator(1, 4)
            ).toString()
        );

        assertEquals(
            "7/20",
            Coefficients.subtract(
                Coefficients.fromNumeratorAndDenominator(-3, -5),
                Coefficients.fromNumeratorAndDenominator(1, 4)
            ).toString()
        );
    }

    @Test
    public void testDivideCoefficients() {
        assertEquals(
            "5/2",
            Coefficients.divide(
                Coefficients.from(5),
                Coefficients.from(2)
            ).toString()
        );

        assertEquals(
            "2",
            Coefficients.divide(
                Coefficients.from(5),
                Coefficients.fromNumeratorAndDenominator(5, 2)
            ).toString()
        );
    }
}
