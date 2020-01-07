package coefficients;

public final class Coefficients {
    private static final Coefficient NEGATIVE_ONE = new IntegerCoefficient(-1);

    private Coefficients() {
        // Exists to defeat instantiation
    }

    public static Coefficient inverse(final Coefficient coefficient) {
        return coefficient.inverse();
    }

    public static Coefficient floor(final Coefficient coefficient) {
        return coefficient.floor();
    }

    public static Coefficient ceil(final Coefficient coefficient) {
        return coefficient.ceil();
    }

    public static Coefficient add(final Coefficient c1, final Coefficient c2) {
        return c1.plus(c2);
    }

    public static Coefficient subtract(final Coefficient c1, final Coefficient c2) {
        return c1.plus(c2.multiply(NEGATIVE_ONE));
    }

    public static Coefficient multiply(final Coefficient c1, final Coefficient c2) {
        return c1.multiply(c2);
    }

    public static Coefficient divide(final Coefficient c1, final Coefficient c2) {
        return c1.multiply(c2.inverse());
    }

    public static Coefficient from(final double value) {
        return new DoubleCoefficient(value);
    }

    public static Coefficient from(final int value) {
        if (value == -1) {
            return negativeOne();
        }

        return new IntegerCoefficient(value);
    }

    public static Coefficient negativeOne() {
        return NEGATIVE_ONE;
    }

    public static Coefficient from(final int numerator, final int denominator) {
        return fromNumeratorAndDenominator(numerator, denominator);
    }

    public static Coefficient fromNumeratorAndDenominator(final int numerator, final int denominator) {
        if (numerator % denominator == 0) {
            return from(numerator / denominator);
        }

        return new RationalCoefficient(numerator, denominator);
    }

    public static double asDouble(final Coefficient coefficient) {
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
}
