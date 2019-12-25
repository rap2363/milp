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
