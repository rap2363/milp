package coefficients;

import javax.sound.sampled.Line;

public final class Coefficients {
    public static final IntegerCoefficient NEGATIVE_ONE = new IntegerCoefficient(-1);

    private Coefficients() {
        // Exists to defeat instantiation
    }

    public static Coefficient add(final Coefficient c1, final Coefficient c2) {
        return c1.plus(c2);
    }

    public static Coefficient subtract(final Coefficient c1, final Coefficient c2) {
        return c1.plus(c2.negate());
    }

    public static ConstantCoefficient multiply(final ConstantCoefficient c1, final ConstantCoefficient c2) {
        return c1.multiply(c2);
    }

    public static ConstantCoefficient divide(final ConstantCoefficient c1, final ConstantCoefficient c2) {
        return c1.multiply(c2.inverse());
    }

    public static DoubleCoefficient from(final double value) {
        return new DoubleCoefficient(value);
    }

    public static IntegerCoefficient from(final int value) {
        if (value == -1) {
            return negativeOne();
        }

        return new IntegerCoefficient(value);
    }

    public static IntegerCoefficient negativeOne() {
        return NEGATIVE_ONE;
    }

    public static Coefficient from(final int numerator, final int denominator) {
        return fromNumeratorAndDenominator(numerator, denominator);
    }

    public static ConstantCoefficient fromNumeratorAndDenominator(final int numerator, final int denominator) {
        if (numerator % denominator == 0) {
            return from(numerator / denominator);
        }

        return new RationalCoefficient(numerator, denominator);
    }

    public static double asDouble(final ConstantCoefficient coefficient) {
        if (coefficient instanceof IntegerCoefficient) {
            return ((IntegerCoefficient) coefficient).getValue();
        } else if (coefficient instanceof DoubleCoefficient) {
            return ((DoubleCoefficient) coefficient).getValue();
        } else if (coefficient instanceof RationalCoefficient) {
            final RationalCoefficient rationalCoefficient = (RationalCoefficient) coefficient;
            return (double) rationalCoefficient.getNumeratorValue() / rationalCoefficient.getDenominatorValue();
        }

        throw new IllegalArgumentException("Invalid Input Coefficient");
    }

    public static boolean isPositive(final Coefficient coefficient) {
        if (coefficient instanceof IntegerCoefficient) {
            return ((IntegerCoefficient) coefficient).getValue() > 0;
        } else if (coefficient instanceof DoubleCoefficient) {
            return ((DoubleCoefficient) coefficient).getValue() > 0d;
        } else if (coefficient instanceof RationalCoefficient) {
            return ((RationalCoefficient) coefficient).getNumeratorValue() > 0;
        }

        throw new IllegalArgumentException("Invalid Input Coefficient");
    }

    public static boolean isZero(final Coefficient coefficient) {
        if (coefficient instanceof IntegerCoefficient) {
            return ((IntegerCoefficient) coefficient).getValue() > 0;
        } else if (coefficient instanceof DoubleCoefficient) {
            return ((DoubleCoefficient) coefficient).getValue() > 0d;
        } else if (coefficient instanceof RationalCoefficient) {
            return ((RationalCoefficient) coefficient).getNumeratorValue() > 0;
        }

        throw new IllegalArgumentException("Invalid Input Coefficient");
    }
}
