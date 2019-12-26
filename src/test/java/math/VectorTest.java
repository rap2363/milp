package math;

import static org.junit.Assert.assertEquals;

import coefficients.Coefficients;
import org.junit.Test;

public class VectorTest {
    @Test
    public void testAddAndDivideVectors() {
        final Vector topVector = new Vector(
            Coefficients.from(2),
            Coefficients.from(1),
            Coefficients.from(1),
            Coefficients.from(0),
            Coefficients.from(0),
            Coefficients.from(18)
        );
        final Vector middleVector = new Vector(
            Coefficients.from(6),
            Coefficients.from(5),
            Coefficients.from(0),
            Coefficients.from(1),
            Coefficients.from(0),
            Coefficients.from(60)
        );
        final Vector bottomVector = new Vector(
            Coefficients.from(2),
            Coefficients.from(5),
            Coefficients.from(0),
            Coefficients.from(0),
            Coefficients.from(1),
            Coefficients.from(40)
        );
        final Vector scaledVector = bottomVector.scale(Coefficients.fromNumeratorAndDenominator(1, 5));
        assertEquals(
            new Vector(
                Coefficients.from(8, 5),
                Coefficients.from(0),
                Coefficients.from(1),
                Coefficients.from(0),
                Coefficients.from(-1, 5),
                Coefficients.from(10)
            ),
            topVector.subtract(scaledVector)
        );
        assertEquals(
            new Vector(
                Coefficients.from(4),
                Coefficients.from(0),
                Coefficients.from(0),
                Coefficients.from(1),
                Coefficients.from(-1),
                Coefficients.from(20)
            ),
            middleVector.subtract(bottomVector)
        );
    }
}
