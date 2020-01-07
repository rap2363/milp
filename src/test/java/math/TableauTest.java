package math;

import static org.junit.Assert.assertEquals;

import coefficients.Coefficients;
import org.junit.Test;

public class TableauTest {
    @Test
    public void testTableauPivot() {
        final Tableau tableau = new Tableau(
            Vector.newBuilder()
                .addCoefficient(18)
                .addCoefficient(2)
                .addCoefficient(1)
                .addCoefficient(1)
                .addCoefficient(0)
                .addCoefficient(0)
                .build(),
            Vector.newBuilder()
                .addCoefficient(60)
                .addCoefficient(6)
                .addCoefficient(5)
                .addCoefficient(0)
                .addCoefficient(1)
                .addCoefficient(0)
                .build(),
            Vector.newBuilder()
                .addCoefficient(40)
                .addCoefficient(2)
                .addCoefficient(5)
                .addCoefficient(0)
                .addCoefficient(0)
                .addCoefficient(1)
                .build(),
            Vector.newBuilder()
                .addCoefficient(0)
                .addCoefficient(-2)
                .addCoefficient(-3)
                .addCoefficient(0)
                .addCoefficient(0)
                .addCoefficient(0)
                .build()
        );

        assertEquals(
            new Tableau(
                Vector.newBuilder()
                    .addCoefficient(10)
                    .addRationalCoefficient(8, 5)
                    .addCoefficient(0)
                    .addCoefficient(1)
                    .addCoefficient(0)
                    .addRationalCoefficient(-1, 5)
                    .build(),
                Vector.newBuilder()
                    .addCoefficient(20)
                    .addCoefficient(4)
                    .addCoefficient(0)
                    .addCoefficient(0)
                    .addCoefficient(1)
                    .addCoefficient(-1)
                    .build(),
                Vector.newBuilder()
                    .addCoefficient(8)
                    .addRationalCoefficient(2, 5)
                    .addCoefficient(1)
                    .addCoefficient(0)
                    .addCoefficient(0)
                    .addRationalCoefficient(1, 5)
                    .build(),
                Vector.newBuilder()
                    .addCoefficient(24)
                    .addRationalCoefficient(-4, 5)
                    .addCoefficient(0)
                    .addCoefficient(0)
                    .addCoefficient(0)
                    .addRationalCoefficient(3, 5)
                    .build()),
            tableau.pivot()
        );
    }

    @Test
    public void testTableauSolve() {
        final Tableau tableau = new Tableau(
            Vector.newBuilder()
                .addCoefficient(18)
                .addCoefficient(2)
                .addCoefficient(1)
                .addCoefficient(1)
                .addCoefficient(0)
                .addCoefficient(0)
                .build(),
            Vector.newBuilder()
                .addCoefficient(60)
                .addCoefficient(6)
                .addCoefficient(5)
                .addCoefficient(0)
                .addCoefficient(1)
                .addCoefficient(0)
                .build(),
            Vector.newBuilder()
                .addCoefficient(40)
                .addCoefficient(2)
                .addCoefficient(5)
                .addCoefficient(0)
                .addCoefficient(0)
                .addCoefficient(1)
                .build(),
            Vector.newBuilder()
                .addCoefficient(0)
                .addCoefficient(-2)
                .addCoefficient(-3)
                .addCoefficient(0)
                .addCoefficient(0)
                .addCoefficient(0)
                .build()
        );

        final Tableau solvedTableau = Tableaus.solve(tableau);

        assertEquals(
            new Tableau(
                Vector.newBuilder()
                    .addCoefficient(2)
                    .addCoefficient(0)
                    .addCoefficient(0)
                    .addCoefficient(1)
                    .addRationalCoefficient(-2, 5)
                    .addRationalCoefficient(1, 5)
                    .build(),
                Vector.newBuilder()
                    .addCoefficient(5)
                    .addCoefficient(1)
                    .addCoefficient(0)
                    .addCoefficient(0)
                    .addRationalCoefficient(1, 4)
                    .addRationalCoefficient(-1, 4)
                    .build(),
                Vector.newBuilder()
                    .addCoefficient(6)
                    .addCoefficient(0)
                    .addCoefficient(1)
                    .addCoefficient(0)
                    .addRationalCoefficient(-1, 10)
                    .addRationalCoefficient(3, 10)
                    .build(),
                Vector.newBuilder()
                    .addCoefficient(28)
                    .addCoefficient(0)
                    .addCoefficient(0)
                    .addCoefficient(0)
                    .addRationalCoefficient(1, 5)
                    .addRationalCoefficient(2, 5)
                    .build()),
            solvedTableau
        );
    }

    @Test
    public void testTableauSolveWithTwoVariables() {
        // Example from http://ncert.nic.in/ncerts/l/lemh206.pdf page 506
        final Tableau tableau = new Tableau(
            Vector.newBuilder()
                .addCoefficient(100)
                .addCoefficient(5)
                .addCoefficient(1)
                .addCoefficient(1)
                .addCoefficient(0)
                .build(),
            Vector.newBuilder()
                .addCoefficient(60)
                .addCoefficient(1)
                .addCoefficient(1)
                .addCoefficient(0)
                .addCoefficient(1)
                .build(),
            Vector.newBuilder()
                .addCoefficient(0)
                .addCoefficient(-250)
                .addCoefficient(-75)
                .addCoefficient(0)
                .addCoefficient(0)
                .build()
        );

        final Tableau solvedTableau = Tableaus.solve(tableau);

        // The answer to the LP is (10, 50), which means the final row will have the objective value of 6250.
        assertEquals(
            6250,
            Coefficients.asDouble(solvedTableau.get(2, 0)),
            1e-10
        );
        assertEquals(
            10,
            Coefficients.asDouble(solvedTableau.get(0, 0)),
            1e-10
        );
        assertEquals(
            50,
            Coefficients.asDouble(solvedTableau.get(1, 0)),
            1e-10
        );
    }

    @Test
    public void testSampleTwoVariableWithFourConstraintsProblem() {
        // See https://personal.utdallas.edu/~scniu/OPRE-6201/documents/LP06-Simplex-Tableau.pdf
        final Tableau tableau = new Tableau(
            Vector.newBuilder()
                .addCoefficient(6)
                .addCoefficient(2)
                .addCoefficient(3)
                .addCoefficient(1)
                .addCoefficient(0)
                .addCoefficient(0)
                .addCoefficient(0)
                .build(),
            Vector.newBuilder()
                .addCoefficient(3)
                .addCoefficient(-3)
                .addCoefficient(2)
                .addCoefficient(0)
                .addCoefficient(1)
                .addCoefficient(0)
                .addCoefficient(0)
                .build(),
            Vector.newBuilder()
                .addCoefficient(5)
                .addCoefficient(0)
                .addCoefficient(2)
                .addCoefficient(0)
                .addCoefficient(0)
                .addCoefficient(1)
                .addCoefficient(0)
                .build(),
            Vector.newBuilder()
                .addCoefficient(4)
                .addCoefficient(2)
                .addCoefficient(1)
                .addCoefficient(0)
                .addCoefficient(0)
                .addCoefficient(0)
                .addCoefficient(1)
                .build(),
            Vector.newBuilder()
                .addCoefficient(0)
                .addCoefficient(-4)
                .addCoefficient(-3)
                .addCoefficient(0)
                .addCoefficient(0)
                .addCoefficient(0)
                .addCoefficient(0)
                .build()
        );

        final Tableau solvedTableau = Tableaus.solve(tableau);

        // The answer to the LP is (3/2, 1), which means the final row will have the objective value of 9.
        assertEquals(
            9,
            Coefficients.asDouble(solvedTableau.get(4, 0)),
            1e-10
        );
        assertEquals(
            1.5,
            Coefficients.asDouble(solvedTableau.get(3, 0)),
            1e-10
        );
        assertEquals(
            1,
            Coefficients.asDouble(solvedTableau.get(0, 0)),
            1e-10
        );
    }

    @Test
    public void testDietProblem() {
        // See http://www.cs.toronto.edu/~anikolov/CSC473W19/MG-LP.pdf
        final Tableau tableau = new Tableau(
            Vector.newBuilder()
                .addCoefficient(100)
                .addCoefficient(5)
                .addCoefficient(1)
                .addCoefficient(1)
                .addCoefficient(0)
                .build(),
            Vector.newBuilder()
                .addCoefficient(60)
                .addCoefficient(1)
                .addCoefficient(1)
                .addCoefficient(0)
                .addCoefficient(1)
                .build(),
            Vector.newBuilder()
                .addCoefficient(0)
                .addCoefficient(-250)
                .addCoefficient(-75)
                .addCoefficient(0)
                .addCoefficient(0)
                .build()
        );

        final Tableau solvedTableau = Tableaus.solve(tableau);
    }
}
