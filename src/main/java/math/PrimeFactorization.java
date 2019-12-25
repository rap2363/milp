package math;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public final class PrimeFactorization {
    private final Map<Integer, Integer> primeFactorsMap;
    private final int value;

    public PrimeFactorization(final int value) {
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

    public PrimeFactorization(final Map<Integer, Integer> primeFactorsMap) {
        this.primeFactorsMap = primeFactorsMap;
        this.value = getValueFrom(primeFactorsMap);
    }

    public Map<Integer, Integer> getPrimeFactorsMap() {
        return primeFactorsMap;
    }

    private static int getValueFrom(final Map<Integer, Integer> primeFactorsMap) {
        int value = 1;
        for (final Entry<Integer, Integer> entry : primeFactorsMap.entrySet()) {
            final int primeFactor = entry.getKey();
            for (int i = 0; i < entry.getValue(); i++) {
                value *= primeFactor;
            }
        }

        return value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    static HashMap<Integer, Integer> getPrimeFactors(final int value) {
        final HashMap<Integer, Integer> primeFactorsMap = new HashMap<>();
        int n = value;
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

    static void incrementMapValue(final HashMap<Integer, Integer> primeFactorsMap, final int value) {
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
