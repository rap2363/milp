package coefficients;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RationalCoefficientTest {
    @Test
    public void testRationalCoefficientCreation() {
        final RationalCoefficient threeFourths = new RationalCoefficient(3, 4);
        assertEquals("3/4", threeFourths.toString());
    }

    @Test
    public void testRationalCoefficientCreationAndSimplification() {
        final RationalCoefficient oneFourth = new RationalCoefficient(15, 60);
        assertEquals("1/4", oneFourth.toString());
    }

    @Test
    public void testRationalCoefficientCreationAndSimplificationForImproperFraction() {
        final RationalCoefficient fiveHalves = new RationalCoefficient(250, 100);
        assertEquals("5/2", fiveHalves.toString());
        assertEquals("2/5", fiveHalves.inverse().toString());
    }

    @Test
    public void testRationalCoefficientInversion() {
        final RationalCoefficient someRational = new RationalCoefficient(30, 50);
        assertEquals("5/3", someRational.inverse().toString());
    }
}
