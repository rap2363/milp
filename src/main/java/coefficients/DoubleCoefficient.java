package coefficients;

public final class DoubleCoefficient implements Coefficient {
    private final double value;

    DoubleCoefficient(final double value) {
        this.value = value;
    }

    double getValue() {
        return value;
    }

    @Override
    public Coefficient inverse() {
        return new DoubleCoefficient(1 / value);
    }

    @Override
    public Coefficient floor() {
        return new IntegerCoefficient((int) value);
    }

    @Override
    public Coefficient ceil() {
        return new IntegerCoefficient((int) value + 1);
    }

    @Override
    public Coefficient multiply(final Coefficient other) {
        if (other instanceof IntegerCoefficient) {
            return new DoubleCoefficient(getValue() * ((IntegerCoefficient) other).getValue());
        } else if (other instanceof DoubleCoefficient) {
            return new DoubleCoefficient(getValue() * ((DoubleCoefficient) other).getValue());
        } else if (other instanceof RationalCoefficient) {
            final RationalCoefficient otherAsRational = (RationalCoefficient) other;
            return new DoubleCoefficient(
                getValue() * otherAsRational.getNumeratorValue() / otherAsRational.getDenominatorValue()
            );
        }

        throw new IllegalArgumentException("Invalid input coefficients");
    }

    @Override
    public Coefficient plus(final Coefficient other) {
        if (other instanceof IntegerCoefficient) {
            return new DoubleCoefficient(getValue() + ((IntegerCoefficient) other).getValue());
        } else if (other instanceof DoubleCoefficient) {
            return new DoubleCoefficient(getValue() + ((DoubleCoefficient) other).getValue());
        } else if (other instanceof RationalCoefficient) {
            final RationalCoefficient otherAsRational = (RationalCoefficient) other;
            return new DoubleCoefficient(
                getValue() + (((double) otherAsRational.getNumeratorValue()) / otherAsRational.getDenominatorValue())
            );
        }

        throw new IllegalArgumentException("Invalid input coefficients");
    }

    @Override
    public int compareTo(final Coefficient other) {
        if (other instanceof IntegerCoefficient) {
            return Double.compare(getValue(), ((IntegerCoefficient) other).getValue());
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
        final DoubleCoefficient that = (DoubleCoefficient) o;
        return Double.compare(that.value, value) == 0;
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }
}
