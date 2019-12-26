package math;

import static org.junit.Assert.assertEquals;

import coefficients.Coefficients;
import org.junit.Test;

public class TableauTest {
    @Test
    public void testTableauPivot() {
        final Tableau tableau = new Tableau(
            new Vector(
                Coefficients.from(18),
                Coefficients.from(2),
                Coefficients.from(1),
                Coefficients.from(1),
                Coefficients.from(0),
                Coefficients.from(0)
            ),
            new Vector(
                Coefficients.from(60),
                Coefficients.from(6),
                Coefficients.from(5),
                Coefficients.from(0),
                Coefficients.from(1),
                Coefficients.from(0)
            ),
            new Vector(
                Coefficients.from(40),
                Coefficients.from(2),
                Coefficients.from(5),
                Coefficients.from(0),
                Coefficients.from(0),
                Coefficients.from(1)
            )
        );

        assertEquals(
            new Tableau(
                new Vector(
                    Coefficients.from(10),
                    Coefficients.from(8, 5),
                    Coefficients.from(0),
                    Coefficients.from(1),
                    Coefficients.from(0),
                    Coefficients.from(-1, 5)
                ),
                new Vector(
                    Coefficients.from(20),
                    Coefficients.from(4),
                    Coefficients.from(0),
                    Coefficients.from(0),
                    Coefficients.from(1),
                    Coefficients.from(-1)
                ),
                new Vector(
                    Coefficients.from(8),
                    Coefficients.from(2, 5),
                    Coefficients.from(1),
                    Coefficients.from(0),
                    Coefficients.from(0),
                    Coefficients.from(1, 5)
                )
            ),
            tableau.pivot(2)
        );
    }
}
