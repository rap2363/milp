package coefficients;

public final class IntegerCoefficient implements ConstantCoefficient {
    private final int value;

    IntegerCoefficient(final int value) {
        this.value = value;
    }

    int getValue() {
        return value;
    }

    @Override
    public IntegerCoefficient floor() {
        return this;
    }

    @Override
    public IntegerCoefficient ceil() {
        return this;
    }

    @Override
    public IntegerCoefficient negate() {
        return new IntegerCoefficient(-this.value);
    }

    @Override
    public Coefficient inverse() {
        if (value == 0) {
            return new DoubleCoefficient(Double.POSITIVE_INFINITY);
        }

        return new RationalCoefficient(1, value);
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
