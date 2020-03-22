package math;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PrimeFactorizationTest {
    @Test
    public void testPrimeFactorizationOf0() {
        final Map<Long, Integer> primeFactorsMap = new PrimeFactorization(0).getPrimeFactorsMap();

        assertEquals(1, primeFactorsMap.size());

        assertEquals(new Integer(1), primeFactorsMap.get(0L));
    }

    @Test
    public void testPrimeFactorizationOf1() {
        final Map<Long, Integer> primeFactorsMap = new PrimeFactorization(1).getPrimeFactorsMap();

        assertEquals(1, primeFactorsMap.size());

        assertEquals(new Integer(1), primeFactorsMap.get(1L));
    }

    @Test
    public void testPrimeFactorizationOf36() {
        final Map<Long, Integer> primeFactorsMap = new PrimeFactorization(36).getPrimeFactorsMap();

        assertEquals(2, primeFactorsMap.size());

        assertEquals(new Integer(2), primeFactorsMap.get(2L));
        assertEquals(new Integer(2), primeFactorsMap.get(3L));
    }

    @Test
    public void testPrimeFactorizationOf120() {
        final Map<Long, Integer> primeFactorsMap = new PrimeFactorization(120).getPrimeFactorsMap();

        assertEquals(3, primeFactorsMap.size());

        assertEquals(new Integer(3), primeFactorsMap.get(2L));
        assertEquals(new Integer(1), primeFactorsMap.get(3L));
        assertEquals(new Integer(1), primeFactorsMap.get(5L));
    }

    @Test
    public void testPrimeFactorizationOf9699690() {
        final Map<Long, Integer> primeFactorsMap = new PrimeFactorization(9699690).getPrimeFactorsMap();

        assertEquals(8, primeFactorsMap.size());

        assertEquals(new Integer(1), primeFactorsMap.get(2L));
        assertEquals(new Integer(1), primeFactorsMap.get(3L));
        assertEquals(new Integer(1), primeFactorsMap.get(5L));
        assertEquals(new Integer(1), primeFactorsMap.get(7L));
        assertEquals(new Integer(1), primeFactorsMap.get(11L));
        assertEquals(new Integer(1), primeFactorsMap.get(13L));
        assertEquals(new Integer(1), primeFactorsMap.get(17L));
        assertEquals(new Integer(1), primeFactorsMap.get(19L));
    }
}
