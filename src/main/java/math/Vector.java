package math;

import coefficients.Coefficient;
import coefficients.Coefficients;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Vector {
    private final Coefficient[] coefficients;

    public Vector(final Coefficient... coefficients) {
        this.coefficients = coefficients;
    }

    public int length() {
        return coefficients.length;
    }

    public Vector add(Vector other) {
        if (coefficients.length != other.length()) {
            throw new IllegalArgumentException("Vectors must be same length");
        }

        final Coefficient[] newCoefficients = new Coefficient[length()];
        for (int i = 0; i < length(); i++) {
            newCoefficients[i] = Coefficients.add(coefficients[i], other.coefficients[i]);
        }

        return new Vector(newCoefficients);
    }

    public Vector subtract(Vector other) {
        return add(other.scale(Coefficients.negativeOne()));
    }

    public Vector scale(Coefficient value) {
        final Coefficient[] newCoefficients = new Coefficient[length()];
        for (int i = 0; i < length(); i++) {
            newCoefficients[i] = Coefficients.multiply(coefficients[i], value);
        }

        return new Vector(newCoefficients);
    }

    public Coefficient get(final int index) {
        return coefficients[index];
    }

    public Coefficient[] getValues() {
        return coefficients;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Vector otherVector = (Vector) o;
        return Arrays.equals(coefficients, otherVector.coefficients);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private final List<Coefficient> coefficients;

        public Builder() {
            this.coefficients = new ArrayList<>();
        }

        public Builder addCoefficient(final int value) {
            this.coefficients.add(Coefficients.from(value));
            return this;
        }

        public Builder addCoefficient(final double value) {
            this.coefficients.add(Coefficients.from(value));
            return this;
        }

        public Builder addRationalCoefficient(final int numerator, final int denominator) {
            this.coefficients.add(Coefficients.from(numerator, denominator));
            return this;
        }

        public Vector build() {
            return new Vector(coefficients.toArray(new Coefficient[0]));
        }
    }
}
