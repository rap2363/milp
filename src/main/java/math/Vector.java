package math;

import coefficients.Coefficient;
import coefficients.Coefficients;
import lang.Preconditions;

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

    public Vector add(final Vector other) {
        if (coefficients.length != other.length()) {
            throw new IllegalArgumentException("Vectors must be same length");
        }

        final Coefficient[] newCoefficients = new Coefficient[length()];
        for (int i = 0; i < length(); i++) {
            newCoefficients[i] = Coefficients.add(coefficients[i], other.coefficients[i]);
        }

        return new Vector(newCoefficients);
    }

    public Vector subtract(final Vector other) {
        return add(other.scale(Coefficients.negativeOne()));
    }

    public Vector scale(final Coefficient value) {
        return scale(this, value);
    }

    public static Vector scale(final Vector vector, final Coefficient value) {
        final int vectorLength = vector.length();
        final Coefficient[] newCoefficients = new Coefficient[vectorLength];
        for (int i = 0; i < vectorLength; i++) {
            newCoefficients[i] = Coefficients.scaleBy(vector.coefficients[i], value);
        }

        return new Vector(newCoefficients);
    }

    public static Vector negate(final Vector vector) {
        return scale(vector, Coefficients.NEGATIVE_ONE);
    }

    public Coefficient dotProduct(final Vector other) {
        return Vector.dotProduct(this, other);
    }

    public static Coefficient dotProduct(final Vector first, final Vector second) {
        Preconditions.checkArgument(first.length() == second.length());
        Coefficient value = Coefficients.ZERO;
        for (int i = 0; i < first.length(); i++) {
            value = Coefficients.add(value, Coefficients.scaleBy(first.get(i), second.get(i)));
        }
        return value;
    }

    public double dotProductAsDouble(final Vector other) {
        return Vector.dotProductAsDouble(this, other);
    }

    public static double dotProductAsDouble(final Vector first, final Vector second) {
        Preconditions.checkArgument(first.length() == second.length());
        double value = 0d;
        for (int i = 0; i < first.length(); i++) {
            value += Coefficients.asDouble(Coefficients.scaleBy(first.get(i), second.get(i)));
        }
        return value;
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

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < coefficients.length; i++) {
            final Coefficient coefficient = coefficients[i];
            stringBuilder.append(coefficient.toString());
            if (i < coefficients.length - 1) {
                stringBuilder.append(",");
            }
        }

        return String.format("[%s]", stringBuilder.toString());
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
            return addCoefficient(Coefficients.from(value));
        }

        public Builder addCoefficient(final double value) {
            return addCoefficient(Coefficients.from(value));
        }

        public Builder addRationalCoefficient(final int numerator, final int denominator) {
            return addCoefficient(Coefficients.from(numerator, denominator));
        }

        public Builder addCoefficient(final Coefficient coefficient) {
            this.coefficients.add(coefficient);
            return this;
        }

        public Builder addAllCoefficients(final Coefficient... coefficients) {
            for (final Coefficient coefficient : coefficients) {
                addCoefficient(coefficient);
            }
            return this;
        }

        public Builder addFromVector(final Vector vector) {
            for (final Coefficient coefficient : vector.getValues()) {
                addCoefficient(coefficient);
            }
            return this;
        }

        public Vector build() {
            return new Vector(coefficients.toArray(new Coefficient[0]));
        }
    }
}
