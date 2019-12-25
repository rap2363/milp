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
