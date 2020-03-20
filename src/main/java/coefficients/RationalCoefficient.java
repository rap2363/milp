package coefficients;

import math.PrimeFactorization;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A coefficient represented by two integer values corresponding to the numerator and denominator.
 */
public final class RationalCoefficient implements ConstantCoefficient {
    private final PrimeFactorization numeratorFactorization;
    private final PrimeFactorization denominatorFactorization;
    private final boolean isNegative;

    RationalCoefficient(final int numeratorValue, final int denominatorValue) {
        this.isNegative = numeratorValue > 0 && denominatorValue < 0 || numeratorValue < 0 && denominatorValue > 0;
        final PrimeFactorization initialNumerator = new PrimeFactorization(Math.abs(numeratorValue));
        final PrimeFactorization initialDenominator = new PrimeFactorization(Math.abs(denominatorValue));

        final Map<Integer, Integer> numeratorMap = initialNumerator.getPrimeFactorsMap();
        final Map<Integer, Integer> denominatorMap = initialDenominator.getPrimeFactorsMap();

        final Set<Integer> allPrimeFactors = new HashSet<>(numeratorMap.keySet());
        allPrimeFactors.addAll(denominatorMap.keySet());

        final Map<Integer, Integer> reducedNumeratorMap = new HashMap<>();
        final Map<Integer, Integer> reducedDenominatorMap = new HashMap<>();

        // For each pair of "intersecting" keys, divide out the values
        for (final int primeFactor : allPrimeFactors) {
            final Integer numeratorCount = numeratorMap.get(primeFactor);
            final Integer denominatorCount = denominatorMap.get(primeFactor);

            if (numeratorCount != null && denominatorCount == null) {
                reducedNumeratorMap.put(primeFactor, numeratorCount);
            } else if (numeratorCount == null && denominatorCount != null) {
                reducedDenominatorMap.put(primeFactor, denominatorCount);
            } else {
                // The numerator and denominator counts will *always* be non-null by this point.
                final int minCount = Math.min(numeratorCount, denominatorCount);

                reducedNumeratorMap.put(primeFactor, numeratorCount - minCount);
                reducedDenominatorMap.put(primeFactor, denominatorCount - minCount);
            }
        }

        this.numeratorFactorization = new PrimeFactorization(reducedNumeratorMap);
        this.denominatorFactorization = new PrimeFactorization(reducedDenominatorMap);
    }

    private RationalCoefficient(final PrimeFactorization numeratorFactorization,
                                final PrimeFactorization denominatorFactorization,
                                final boolean isNegative) {
        this.numeratorFactorization = numeratorFactorization;
        this.denominatorFactorization = denominatorFactorization;
        this.isNegative = isNegative;
    }

    public int getNumeratorValue() {
        return numeratorFactorization.getValue() * (isNegative ? -1 : 1);
    }

    public int getDenominatorValue() {
        return denominatorFactorization.getValue();
    }

    @Override
    public ConstantCoefficient floor() {
        return new IntegerCoefficient(numeratorFactorization.getValue() / denominatorFactorization.getValue());
    }

    @Override
    public ConstantCoefficient ceil() {
        return new IntegerCoefficient((numeratorFactorization.getValue() / denominatorFactorization.getValue()) + 1);
    }

    @Override
    public RationalCoefficient negate() {
        return new RationalCoefficient(this.numeratorFactorization, this.denominatorFactorization, !isNegative);
    }

    @Override
    public Coefficient inverse() {
        return Coefficients.fromNumeratorAndDenominator(
                this.getDenominatorValue(),
                this.getNumeratorValue()
        );
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final RationalCoefficient that = (RationalCoefficient) o;
        return numeratorFactorization.equals(that.numeratorFactorization)
                && denominatorFactorization.equals(that.denominatorFactorization);
    }

    @Override
    public String toString() {
        return getNumeratorValue() + "/" + getDenominatorValue();
    }
}
