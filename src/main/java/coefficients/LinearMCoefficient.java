package coefficients;

/**
 * A coefficient represented by two constant coefficients (a,b) corresponding to the following linear function:
 * aM + b in the limit of large, positive M. This is useful for using the Big-M method for solving generalized
 * simplexes with inequality and equality constraints.
 *
 * Two linear coefficients can be compared by first comparing their a-values, and if these are equal, then their b
 * values are compared.
 *
 * A LinearMCoefficient generalizes any constant coefficients, x (e.g. (0, x)), which makes comparison relatively
 * straightforward using the previously outlined coefficient rules.
 */
public final class LinearMCoefficient implements Coefficient {
    private final ConstantCoefficient slopeValue;
    private final ConstantCoefficient interceptValue;

    public LinearMCoefficient(final ConstantCoefficient slopeValue, final ConstantCoefficient interceptValue) {
        this.slopeValue = slopeValue;
        this.interceptValue = interceptValue;
    }

    @Override
    public Coefficient plus(Coefficient other) {
        return null;
    }

    @Override
    public LinearMCoefficient negate() {
        return new LinearMCoefficient(slopeValue.negate(), interceptValue.negate());
    }

    @Override
    public int compareTo(Coefficient other) {
        if (other instanceof IntegerCoefficient) {
            final double slopeCoefficient
                    = Coefficients.isPositive(slopeValue) ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
            return Double.compare(slopeCoefficient, ((IntegerCoefficient) other).getValue());
        } else if (other instanceof DoubleCoefficient) {
            final double slopeCoefficient
                    = Coefficients.isPositive(slopeValue) ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
            return Double.compare(slopeCoefficient, ((DoubleCoefficient) other).getValue());
        } else if (other instanceof RationalCoefficient) {
            final double slopeCoefficient
                    = Coefficients.isPositive(slopeValue) ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
            final RationalCoefficient otherAsRational = (RationalCoefficient) other;
            return Double.compare(
                    slopeCoefficient,
                    (double) otherAsRational.getNumeratorValue() / otherAsRational.getDenominatorValue()
            );
        } else if (other instanceof LinearMCoefficient) {
            final LinearMCoefficient otherAsLinearCoefficient = (LinearMCoefficient) other;
            // First compare slope values
            final int compareValue = this.slopeValue.compareTo(otherAsLinearCoefficient.getSlopeValue());

            if (compareValue != 0) {
                return compareValue;
            }

            return this.interceptValue.compareTo(otherAsLinearCoefficient.getInterceptValue());
        }

        return 0;
    }

    final ConstantCoefficient getSlopeValue() {
        return slopeValue;
    }

    private Coefficient getInterceptValue() {
        return interceptValue;
    }
}
