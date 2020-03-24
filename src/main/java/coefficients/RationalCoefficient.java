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

    RationalCoefficient(final long numeratorValue, final long denominatorValue) {
        this.isNegative = numeratorValue > 0 && denominatorValue < 0 || numeratorValue < 0 && denominatorValue > 0;
        final PrimeFactorization initialNumerator = new PrimeFactorization(Math.abs(numeratorValue));
        final PrimeFactorization initialDenominator = new PrimeFactorization(Math.abs(denominatorValue));

        final Map<Long, Integer> numeratorMap = initialNumerator.getPrimeFactorsMap();
        final Map<Long, Integer> denominatorMap = initialDenominator.getPrimeFactorsMap();

        final Set<Long> allPrimeFactors = new HashSet<>(numeratorMap.keySet());
        allPrimeFactors.addAll(denominatorMap.keySet());

        final Map<Long, Integer> reducedNumeratorMap = new HashMap<>();
        final Map<Long, Integer> reducedDenominatorMap = new HashMap<>();

        // For each pair of "intersecting" keys, divide out the values
        for (final long primeFactor : allPrimeFactors) {
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

    public static RationalCoefficient multiply(final RationalCoefficient firstRationalCoefficient,
                                               final RationalCoefficient secondRationalCoefficient) {
        final boolean newCoefficientIsNegative
                = firstRationalCoefficient.isNegative && !secondRationalCoefficient.isNegative
                || !firstRationalCoefficient.isNegative && secondRationalCoefficient.isNegative;

        final PrimeFactorization newNumeratorFactorization = PrimeFactorization.concatenate(
                firstRationalCoefficient.numeratorFactorization,
                secondRationalCoefficient.numeratorFactorization
        );

        final PrimeFactorization newDenominatorFactorization = PrimeFactorization.concatenate(
                firstRationalCoefficient.denominatorFactorization,
                secondRationalCoefficient.denominatorFactorization
        );

        final Set<Long> allPrimeFactors = new HashSet<>(newNumeratorFactorization.getPrimeFactorsMap().keySet());
        allPrimeFactors.addAll(newDenominatorFactorization.getPrimeFactorsMap().keySet());

        final Map<Long, Integer> reducedNumeratorMap = new HashMap<>();
        final Map<Long, Integer> reducedDenominatorMap = new HashMap<>();

        // For each pair of "intersecting" keys, divide out the values
        for (final long primeFactor : allPrimeFactors) {
            final Integer numeratorCount = newNumeratorFactorization.getPrimeFactorsMap().get(primeFactor);
            final Integer denominatorCount = newDenominatorFactorization.getPrimeFactorsMap().get(primeFactor);

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

        return new RationalCoefficient(
                new PrimeFactorization(reducedNumeratorMap),
                new PrimeFactorization(reducedDenominatorMap),
                newCoefficientIsNegative);
    }

    public long getNumeratorValue() {
        return numeratorFactorization.getValue() * (isNegative ? -1 : 1);
    }

    public long getDenominatorValue() {
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
