package coefficients;

public final class Coefficients {
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

    /**
     * Dispatch on the multiplications of all 9 possible combinations of coefficient types.
     */
    public static Coefficient multiply(final Coefficient c1, final Coefficient c2) {
        if (c1 instanceof IntegerCoefficient && c2 instanceof IntegerCoefficient) {
            return new IntegerCoefficient(((IntegerCoefficient) c1).getValue() * ((IntegerCoefficient) c2).getValue());
        } else if (c1 instanceof IntegerCoefficient && c2 instanceof DoubleCoefficient) {
            return new DoubleCoefficient(((IntegerCoefficient) c1).getValue() * ((DoubleCoefficient) c2).getValue());
        } else if (c1 instanceof IntegerCoefficient && c2 instanceof RationalCoefficient) {
            final RationalCoefficient c2AsRational = (RationalCoefficient) c2;
            return fromNumeratorAndDenominator(
                ((IntegerCoefficient) c1).getValue() * c2AsRational.getNumeratorValue(),
                c2AsRational.getDenominatorValue()
            );
        } else if (c1 instanceof DoubleCoefficient && c2 instanceof IntegerCoefficient) {
            return new DoubleCoefficient(((DoubleCoefficient) c1).getValue() * ((IntegerCoefficient) c2).getValue());
        } else if (c1 instanceof DoubleCoefficient && c2 instanceof DoubleCoefficient) {
            return new DoubleCoefficient(((DoubleCoefficient) c1).getValue() * ((DoubleCoefficient) c2).getValue());
        } else if (c1 instanceof DoubleCoefficient && c2 instanceof RationalCoefficient) {
            final RationalCoefficient c2AsRational = (RationalCoefficient) c2;
            return new DoubleCoefficient(
                ((DoubleCoefficient) c1).getValue()
                    * c2AsRational.getNumeratorValue()
                    / c2AsRational.getDenominatorValue()
            );
        } else if (c1 instanceof RationalCoefficient && c2 instanceof IntegerCoefficient) {
            final RationalCoefficient c1AsRational = (RationalCoefficient) c1;
            return fromNumeratorAndDenominator(
                c1AsRational.getNumeratorValue() * ((IntegerCoefficient) c2).getValue(),
                c1AsRational.getDenominatorValue()
            );
        } else if (c1 instanceof RationalCoefficient && c2 instanceof DoubleCoefficient) {
            final RationalCoefficient c1AsRational = (RationalCoefficient) c1;
            return new DoubleCoefficient(
                c1AsRational.getNumeratorValue()
                    * ((DoubleCoefficient) c2).getValue()
                    / c1AsRational.getDenominatorValue()
            );
        } else if (c1 instanceof RationalCoefficient && c2 instanceof RationalCoefficient) {
            final RationalCoefficient c1AsRational = (RationalCoefficient) c1;
            final RationalCoefficient c2AsRational = (RationalCoefficient) c2;

            return fromNumeratorAndDenominator(
                c1AsRational.getNumeratorValue() * c2AsRational.getNumeratorValue(),
                c1AsRational.getDenominatorValue() * c2AsRational.getDenominatorValue()
            );
        }

        throw new IllegalArgumentException("Invalid input coefficients");
    }

    public static Coefficient fromNumeratorAndDenominator(final int numerator, final int denominator) {
        if (numerator % denominator == 0) {
            return new IntegerCoefficient(numerator / denominator);
        }
        return new RationalCoefficient(numerator, denominator);
    }
}
