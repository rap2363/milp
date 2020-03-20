package coefficients;

/**
 * A coefficient represented by two constant coefficients (a,b) corresponding to the following linear function:
 * aM + b in the limit of large, positive M. This is useful for using the Big-M method for solving generalized
 * simplexes with inequality and equality constraints.
 * <p>
 * Two linear coefficients can be compared by first comparing their a-values, and if these are equal, then their b
 * values are compared.
 * <p>
 * A LinearMCoefficient generalizes any constant coefficients, x (e.g. (0, x)), which makes comparison relatively
 * straightforward using the previously outlined coefficient rules.
 */
public final class LinearMCoefficient implements Coefficient {
    private final ConstantCoefficient slopeValue;
    private final ConstantCoefficient interceptValue;

    public LinearMCoefficient(final ConstantCoefficient slopeValue, final ConstantCoefficient interceptValue) {
        if (Coefficients.isZero(slopeValue)) {
            throw new IllegalArgumentException("Slope value must be non-zero!");
        }
        this.slopeValue = slopeValue;
        this.interceptValue = interceptValue;
    }

    @Override
    public LinearMCoefficient negate() {
        return new LinearMCoefficient(slopeValue.negate(), interceptValue.negate());
    }

    @Override
    public Coefficient inverse() {
        return Coefficients.ZERO;
    }

    public ConstantCoefficient getSlopeValue() {
        return slopeValue;
    }

    public ConstantCoefficient getInterceptValue() {
        return interceptValue;
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final LinearMCoefficient that = (LinearMCoefficient) o;
        return that.getSlopeValue().equals(this.getSlopeValue())
                && that.getInterceptValue().equals(this.getInterceptValue());
    }

    @Override
    public String toString() {
        final boolean positiveIntercept = Coefficients.isPositive(getInterceptValue());
        final boolean isZeroIntercept = Coefficients.isZero(getInterceptValue());

        if (positiveIntercept) {
            return String.format("%sM + %s", getSlopeValue(), getInterceptValue());
        } else if (isZeroIntercept) {
            return String.format("%sM", getSlopeValue());
        } else {
            return String.format("%sM - %s", getSlopeValue(), getInterceptValue().negate());
        }
    }
}
