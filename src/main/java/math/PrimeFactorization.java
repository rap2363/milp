package math;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public final class PrimeFactorization {
    private final Map<Long, Integer> primeFactorsMap;
    private final long value;

    public PrimeFactorization(final long value) {
        if (value < 0) {
            throw new IllegalArgumentException("Value can't be negative");
        } else if (value <= 1) {
            primeFactorsMap = new HashMap<>();
            primeFactorsMap.put(value, 1);
        } else {
            primeFactorsMap = getPrimeFactors(value);
        }
        this.value = value;
    }

    public PrimeFactorization(final Map<Long, Integer> primeFactorsMap) {
        this.primeFactorsMap = primeFactorsMap;
        this.value = getValueFrom(primeFactorsMap);
    }

    public Map<Long, Integer> getPrimeFactorsMap() {
        return primeFactorsMap;
    }

    private static int getValueFrom(final Map<Long, Integer> primeFactorsMap) {
        int value = 1;
        for (final Entry<Long, Integer> entry : primeFactorsMap.entrySet()) {
            final long primeFactor = entry.getKey();
            for (int i = 0; i < entry.getValue(); i++) {
                value *= primeFactor;
            }
        }

        return value;
    }

    /**
     * Concatenate two prime factorization maps (which multiplies two prime factorizations)
     */
    public static PrimeFactorization concatenate(final PrimeFactorization firstPrimeFactorization,
                                                 final PrimeFactorization secondPrimeFactorization) {
        final Map<Long, Integer> firstMap = firstPrimeFactorization.getPrimeFactorsMap();
        final Map<Long, Integer> secondMap = secondPrimeFactorization.getPrimeFactorsMap();
        final Map<Long, Integer> newPrimeFactorsMap = new HashMap<>(firstMap);

        secondMap.forEach((k, v) -> newPrimeFactorsMap.merge(k, v, Integer::sum));

        return new PrimeFactorization(newPrimeFactorsMap);
    }

    public long getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Long.toString(value);
    }

    static HashMap<Long, Integer> getPrimeFactors(final long value) {
        final HashMap<Long, Integer> primeFactorsMap = new HashMap<>();
        long n = value;
        while (n % 2 == 0) {
            incrementMapValue(primeFactorsMap, 2);
            n /= 2;
        }

        for (int i = 3; i <= Math.sqrt(n); i += 2) {
            while (n % i == 0) {
                incrementMapValue(primeFactorsMap, i);
                n /= i;
            }
        }

        if (n > 2) {
            incrementMapValue(primeFactorsMap, n);
        }

        return primeFactorsMap;
    }

    private static void incrementMapValue(final Map<Long, Integer> primeFactorsMap, final long value) {
        if (!primeFactorsMap.containsKey(value)) {
            primeFactorsMap.put(value, 1);
        } else {
            primeFactorsMap.put(value, primeFactorsMap.get(value) + 1);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PrimeFactorization that = (PrimeFactorization) o;
        return value == that.value;
    }
}
