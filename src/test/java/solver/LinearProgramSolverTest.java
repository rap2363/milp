package solver;

import coefficients.Coefficients;
import math.Vector;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class LinearProgramSolverTest {

    @Test
    public void testLPSolveForStiglersNutritionModel() {
        /*
         * This is a huge instance of the diet problem with 9 equations and 20 variables.
         *
         * See slide 13: https://imada.sdu.dk/~marco/Teaching/AY2017-2018/DM559/Slides/dm545-lec2.pdf
         * Original Instance: https://www.gams.com/latest/gamslib_ml/libhtml/gamslib_diet.html
         * Wiki: https://en.wikipedia.org/wiki/Stigler_diet
         *
         * Roughly, the problem is formulated as such:
         *
         * Minimize the cost (in $) of a diet for a 154 pound man who needs some minimum requirement of 9 essential
         * nutrients, given that he can select from 20 food items (each of which cost some amount). Note, the original
         * problem actually had 77 types of food, but Stigler used some heuristics to eliminate 57 food types that
         * were strictly dominated in cost and nutritional value by the other foods (I imagine).
         */

        /*
         * Parameter b(n) 'required daily allowances of nutrients'
         *                / calorie     3,   protein   70, calcium      .8
         *                  iron       12,   vitamin-a  5, vitamin-b1  1.8
         *                  vitamin-b2  2.7, niacin    18, vitamin-c  75   /;
         *
         * Table a(f,n) 'nutritive value of foods (per dollar spent)'
         *                 calorie  protein  calcium  iron  vitamin-a  vitamin-b1  vitamin-b2  niacin  vitamin-c
         *                  (1000)      (g)      (g)  (mg)    1000iu)        (mg)        (mg)    (mg)       (mg)
         *    wheat           44.7     1411      2.0   365                   55.4        33.3     441
         *    cornmeal        36        897      1.7    99       30.9        17.4         7.9     106
         *    cannedmilk       8.4      422     15.1     9       26           3          23.5      11         60
         *    margarine       20.6       17       .6     6       55.8          .2
         *    cheese           7.4      448     16.4    19       28.1          .8        10.3       4
         *    peanut-b        15.7      661      1      48                    9.6         8.1     471
         *    lard            41.7                                 .2                      .5       5
         *    liver            2.2      333       .2   139      169.2         6.4        50.8     316        525
         *    porkroast        4.4      249       .3    37                   18.2         3.6      79
         *    salmon           5.8      705      6.8    45        3.5         1           4.9     209
         *    greenbeans       2.4      138      3.7    80       69           4.3         5.8      37        862
         *    cabbage          2.6      125      4      36        7.2         9           4.5      26       5369
         *    onions           5.8      166      3.8    59       16.6         4.7         5.9      21       1184
         *    potatoes        14.3      336      1.8   118        6.7        29.4         7.1     198       2522
         *    spinach          1.1      106            138      918.4         5.7        13.8      33       2755
         *    sweet-pot        9.6      138      2.7    54      290.7         8.4         5.4      83       1912
         *    peaches          8.5       87      1.7   173       86.8         1.2         4.3      55         57
         *    prunes          12.8       99      2.5   154       85.7         3.9         4.3      65        257
         *    limabeans       17.4     1055      3.7   459        5.1        26.9        38.2      93
         *    navybeans       26.9     1691     11.4   792                   38.4        24.6     217           ;
         */

        final LinearProgramSolver solver = LinearProgramSolver.newBuilder()
                .minimizingCost()
                // Costs are normalized across each food (e.g. the units are in /$).
                .withCostVector(Vector.newBuilder()
                        .addAllCoefficients(
                                // 20 foods
                                Coefficients.ONE,
                                Coefficients.ONE,
                                Coefficients.ONE,
                                Coefficients.ONE,
                                Coefficients.ONE,
                                Coefficients.ONE,
                                Coefficients.ONE,
                                Coefficients.ONE,
                                Coefficients.ONE,
                                Coefficients.ONE,
                                Coefficients.ONE,
                                Coefficients.ONE,
                                Coefficients.ONE,
                                Coefficients.ONE,
                                Coefficients.ONE,
                                Coefficients.ONE,
                                Coefficients.ONE,
                                Coefficients.ONE,
                                Coefficients.ONE,
                                Coefficients.ONE)
                        .build())
                // Add the nutritional requirements first
                // calorie
                .addGreaterThanInequality(
                        Coefficients.from(3),
                        44.7, 36, 8.4, 20.6, 7.4, 15.7, 41.7, 2.2, 4.4, 5.8, 2.4, 2.6, 5.8, 14.3, 1.1, 9.6, 8.5, 12.8, 17.4, 26.9
                )
                // protein
                .addGreaterThanInequality(
                        Coefficients.from(70),
                        1411, 897, 422, 17, 448, 661, 0, 333, 249, 705, 138, 125, 166, 336, 106, 138, 87, 99, 1055, 1691
                )
                // calcium
                .addGreaterThanInequality(
                        Coefficients.from(0.8),
                        2.0, 1.7, 15.1, 0.6, 16.4, 1, 0, 0.2, 0.3, 6.8, 3.7, 4, 3.8, 1.8, 0, 2.7, 1.7, 2.5, 3.7, 11.4
                )
                // iron
                .addGreaterThanInequality(
                        Coefficients.from(12),
                        365, 99, 9, 6, 19, 48, 0, 139, 37, 45, 80, 36, 59, 118, 138, 54, 173, 154, 459, 792
                )
                // vitamin-a
                .addGreaterThanInequality(
                        Coefficients.from(5),
                        0, 30.9, 26, 55.8, 28.1, 0, 0.2, 169.2, 0, 3.5, 69, 7.2, 16.6, 6.7, 918.4, 290.7, 86.8, 85.7, 5.1, 0
                )
                // vitamin-b1
                .addGreaterThanInequality(
                        Coefficients.from(1.8),
                        55.4, 17.4, 3, .2, .8, 9.6, 0, 6.4, 18.2, 1, 4.3, 9, 4.7, 29.4, 5.7, 8.4, 1.2, 3.9, 26.9, 38.4
                )
                // vitamin-b2
                .addGreaterThanInequality(
                        Coefficients.from(2.7),
                        33.3, 7.9, 23.5, 0, 10.3, 8.1, .5, 50.8, 3.6, 4.9, 5.8, 4.5, 5.9, 7.1, 13.8, 5.4, 4.3, 4.3, 38.2, 24.6
                )
                // niacin
                .addGreaterThanInequality(
                        Coefficients.from(18),
                        441, 106, 11, 0, 4, 471, 5, 316, 79, 209, 37, 26, 21, 198, 33, 83, 55, 65, 93, 217
                )
                // vitamin-c
                .addGreaterThanInequality(
                        Coefficients.from(75),
                        0, 0, 60, 0, 0, 0, 0, 525, 0, 0, 862, 5369, 1184, 2522, 2755, 1912, 57, 257, 0, 0
                )
                .build();

        assertTrue(solver.getOptimalSolutionIfFeasible().isPresent());
    }
}
