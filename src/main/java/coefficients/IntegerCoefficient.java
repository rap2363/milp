package coefficients;

public final class IntegerCoefficient implements Coefficient {
    private final int value;

    IntegerCoefficient(final int value) {
        this.value = value;
    }

    int getValue() {
        return value;
    }

    @Override
    public Coefficient inverse() {
        return new RationalCoefficient(1, value);
    }

    @Override
    public Coefficient floor() {
        return this;
    }

    @Override
    public Coefficient ceil() {
        return this;
    }

    @Override
    public Coefficient multiply(final Coefficient other) {
        if (other instanceof IntegerCoefficient) {
            return new IntegerCoefficient(getValue() * ((IntegerCoefficient) other).getValue());
        } else if (other instanceof DoubleCoefficient) {
            return new DoubleCoefficient(getValue() * ((DoubleCoefficient) other).getValue());
        } else if (other instanceof RationalCoefficient) {
            final RationalCoefficient otherAsRational = (RationalCoefficient) other;
            return Coefficients.fromNumeratorAndDenominator(
                getValue() * otherAsRational.getNumeratorValue(), otherAsRational.getDenominatorValue()
            );
        }

        throw new IllegalArgumentException("Invalid input coefficients");
    }

    @Override
    public Coefficient plus(final Coefficient other) {
        if (other instanceof IntegerCoefficient) {
            return new IntegerCoefficient(getValue() + ((IntegerCoefficient) other).getValue());
        } else if (other instanceof DoubleCoefficient) {
            return new DoubleCoefficient(getValue() + ((DoubleCoefficient) other).getValue());
        } else if (other instanceof RationalCoefficient) {
            final RationalCoefficient otherAsRational = (RationalCoefficient) other;
            return Coefficients.fromNumeratorAndDenominator(
                getValue() * otherAsRational.getDenominatorValue() + otherAsRational.getNumeratorValue(),
                otherAsRational.getDenominatorValue()
            );
        }

        throw new IllegalArgumentException("Invalid input coefficients");
    }

    @Override
    public int compareTo(final Coefficient other) {
        if (other instanceof IntegerCoefficient) {
            return Integer.compare(getValue(), ((IntegerCoefficient) other).getValue());
        } else if (other instanceof DoubleCoefficient) {
            return Double.compare(getValue(), ((DoubleCoefficient) other).getValue());
        } else if (other instanceof RationalCoefficient) {
            final RationalCoefficient otherAsRational = (RationalCoefficient) other;
            return Double.compare(
                getValue(),
                (double) otherAsRational.getNumeratorValue() / otherAsRational.getDenominatorValue()
            );
        }

        throw new IllegalArgumentException("Invalid input coefficients");
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final IntegerCoefficient that = (IntegerCoefficient) o;
        return value == that.value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
