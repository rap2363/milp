package coefficients;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
    public void testScaleIntByInt() {
        assertEquals(
                "-12",
                Coefficients.scaleBy(new IntegerCoefficient(-3), new IntegerCoefficient(4)).toString()
        );
    }

    @Test
    public void testScaleIntByDouble() {
        assertEquals(
                "-13.5",
                Coefficients.scaleBy(new IntegerCoefficient(-3), new DoubleCoefficient(4.5)).toString()
        );
    }

    @Test
    public void testScaleIntByRational() {
        assertEquals(
                "2",
                Coefficients.scaleBy(new IntegerCoefficient(-3), new RationalCoefficient(-4, 6)).toString()
        );

        assertEquals(
                "-3/4",
                Coefficients.scaleBy(new IntegerCoefficient(-6), new IntegerCoefficient(8).inverse()).toString()
        );

        assertEquals(
                "1",
                Coefficients.scaleBy(new IntegerCoefficient(6), new IntegerCoefficient(6).inverse()).toString()
        );
    }

    @Test
    public void testScaleIntByLinear() {
        assertEquals(
                "2M + 10",
                Coefficients.scaleBy(
                        new IntegerCoefficient(2),
                        new LinearMCoefficient(
                                new IntegerCoefficient(1),
                                new IntegerCoefficient(5)
                        ))
                        .toString()
        );

        assertEquals(
                "-1M - 3",
                Coefficients.scaleBy(
                        new IntegerCoefficient(-1),
                        new LinearMCoefficient(
                                new IntegerCoefficient(1),
                                new IntegerCoefficient(3)
                        ))
                        .toString()
        );

        assertEquals(
                "-1M - 3",
                Coefficients.scaleBy(
                        new LinearMCoefficient(
                                new IntegerCoefficient(1),
                                new IntegerCoefficient(3)
                        ),
                        new IntegerCoefficient(-1))
                        .toString()
        );
    }

    @Test
    public void testScaleDoubleByLinear() {
        assertEquals(
                "-7.0M + 12.25",
                Coefficients.scaleBy(
                        new LinearMCoefficient(
                                new IntegerCoefficient(-2),
                                new DoubleCoefficient(3.5)
                        ),
                        new DoubleCoefficient(3.5))
                        .toString()
        );
    }

    @Test
    public void testScaleRationalByLinear() {
        assertEquals(
                "1/2M - 10",
                Coefficients.scaleBy(
                        new LinearMCoefficient(
                                new IntegerCoefficient(2),
                                new IntegerCoefficient(-40)
                        ),
                        new RationalCoefficient(1, 4))
                        .toString()
        );

        assertEquals(
                "5/4M - 3/2",
                Coefficients.scaleBy(
                        new LinearMCoefficient(
                                new RationalCoefficient(1, 2),
                                new RationalCoefficient(-3, 5)
                        ),
                        new RationalCoefficient(5, 2))
                        .toString()
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
    public void testAddIntToLinear() {
        assertEquals(
                "2M + 10",
                Coefficients.add(
                        new IntegerCoefficient(-3),
                        new LinearMCoefficient(
                                new IntegerCoefficient(2),
                                new IntegerCoefficient(13))
                ).toString()
        );

        assertEquals(
                "2M - 15",
                Coefficients.add(
                        new IntegerCoefficient(1),
                        new LinearMCoefficient(
                                new IntegerCoefficient(2),
                                new IntegerCoefficient(-16))
                ).toString()
        );
    }

    @Test
    public void testAddDoubleToLinear() {
        assertEquals(
                "2M + 10.0",
                Coefficients.add(
                        new DoubleCoefficient(-3),
                        new LinearMCoefficient(
                                new IntegerCoefficient(2),
                                new IntegerCoefficient(13))
                ).toString()
        );

        assertEquals(
                "2M - 15.0",
                Coefficients.add(
                        new DoubleCoefficient(1),
                        new LinearMCoefficient(
                                new IntegerCoefficient(2),
                                new IntegerCoefficient(-16))
                ).toString()
        );
    }

    @Test
    public void testAddRationalToLinear() {
        assertEquals(
                "2M + 3/5",
                Coefficients.add(
                        new RationalCoefficient(1, 5),
                        new LinearMCoefficient(
                                new IntegerCoefficient(2),
                                new RationalCoefficient(2, 5)))
                        .toString()
        );
    }

    @Test
    public void testAddLinearToLinear() {
        assertEquals(
                "4M + 26",
                Coefficients.add(
                        new LinearMCoefficient(
                                new IntegerCoefficient(2),
                                new IntegerCoefficient(13)
                        ),
                        new LinearMCoefficient(
                                new IntegerCoefficient(2),
                                new IntegerCoefficient(13))
                ).toString()
        );

        assertEquals(
                new IntegerCoefficient(26),
                Coefficients.add(
                        new LinearMCoefficient(
                                new IntegerCoefficient(2),
                                new IntegerCoefficient(13)
                        ),
                        new LinearMCoefficient(
                                new IntegerCoefficient(-2),
                                new IntegerCoefficient(13))));
    }

    @Test
    public void testSubtractLinearFromLinear() {
        assertEquals(
                new IntegerCoefficient(0),
                Coefficients.subtract(new LinearMCoefficient(
                                new IntegerCoefficient(2),
                                new IntegerCoefficient(13)
                        ),
                        new LinearMCoefficient(
                                new IntegerCoefficient(2),
                                new IntegerCoefficient(13))));
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

        assertEquals(
                Coefficients.ZERO,
                Coefficients.divide(
                        new IntegerCoefficient(3),
                        new LinearMCoefficient(new IntegerCoefficient(1), new IntegerCoefficient(10))
                )
        );

        assertEquals(
                Coefficients.ZERO,
                Coefficients.divide(
                        new IntegerCoefficient(-10),
                        new LinearMCoefficient(new IntegerCoefficient(-1), new IntegerCoefficient(10))
                )
        );

        assertEquals(
                "3M - 2",
                Coefficients.divide(
                        new LinearMCoefficient(new IntegerCoefficient(15), new IntegerCoefficient(-10)),
                        new IntegerCoefficient(5)
                ).toString()
        );
    }
}
