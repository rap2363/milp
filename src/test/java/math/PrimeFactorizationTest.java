package math;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import org.junit.Test;

public class PrimeFactorizationTest {
    @Test
    public void testPrimeFactorizationOf0() {
        final Map<Integer, Integer> primeFactorsMap = new PrimeFactorization(0).getPrimeFactorsMap();

        assertEquals(1, primeFactorsMap.size());

        assertEquals(new Integer(1), primeFactorsMap.get(0));
    }

    @Test
    public void testPrimeFactorizationOf1() {
        final Map<Integer, Integer> primeFactorsMap = new PrimeFactorization(1).getPrimeFactorsMap();

        assertEquals(1, primeFactorsMap.size());

        assertEquals(new Integer(1), primeFactorsMap.get(1));
    }

    @Test
    public void testPrimeFactorizationOf36() {
        final Map<Integer, Integer> primeFactorsMap = new PrimeFactorization(36).getPrimeFactorsMap();

        assertEquals(2, primeFactorsMap.size());

        assertEquals(new Integer(2), primeFactorsMap.get(2));
        assertEquals(new Integer(2), primeFactorsMap.get(3));
    }

    @Test
    public void testPrimeFactorizationOf120() {
        final Map<Integer, Integer> primeFactorsMap = new PrimeFactorization(120).getPrimeFactorsMap();

        assertEquals(3, primeFactorsMap.size());

        assertEquals(new Integer(3), primeFactorsMap.get(2));
        assertEquals(new Integer(1), primeFactorsMap.get(3));
        assertEquals(new Integer(1), primeFactorsMap.get(5));
    }

    @Test
    public void testPrimeFactorizationOf9699690() {
        final Map<Integer, Integer> primeFactorsMap = new PrimeFactorization(9699690).getPrimeFactorsMap();

        assertEquals(8, primeFactorsMap.size());

        assertEquals(new Integer(1), primeFactorsMap.get(2));
        assertEquals(new Integer(1), primeFactorsMap.get(3));
        assertEquals(new Integer(1), primeFactorsMap.get(5));
        assertEquals(new Integer(1), primeFactorsMap.get(7));
        assertEquals(new Integer(1), primeFactorsMap.get(11));
        assertEquals(new Integer(1), primeFactorsMap.get(13));
        assertEquals(new Integer(1), primeFactorsMap.get(17));
        assertEquals(new Integer(1), primeFactorsMap.get(19));
    }
}
