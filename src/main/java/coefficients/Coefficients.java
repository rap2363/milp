package coefficients;

import java.util.HashMap;
import java.util.Map;

public final class Coefficients {
    private static final Map<Class<? extends Coefficient>, Integer> PRIORITY_MAP;

    static {
        PRIORITY_MAP = new HashMap<>();
        PRIORITY_MAP.put(LinearMCoefficient.class, 0);
        PRIORITY_MAP.put(RationalCoefficient.class, 1);
        PRIORITY_MAP.put(DoubleCoefficient.class, 2);
        PRIORITY_MAP.put(IntegerCoefficient.class, 3);
    }

    public static final IntegerCoefficient ZERO = new IntegerCoefficient(0);
    public static final IntegerCoefficient NEGATIVE_ONE = new IntegerCoefficient(-1);
    public static final IntegerCoefficient ONE = new IntegerCoefficient(1);

    private Coefficients() {
        // Exists to defeat instantiation
    }

    public static Coefficient subtract(final Coefficient c1, final Coefficient c2) {
        return Coefficients.add(c1, c2.negate());
    }

    public static Coefficient divide(final Coefficient c1, final Coefficient c2) {
        return Coefficients.scaleBy(c1, c2.inverse());
    }

    public static Coefficient invert(final Coefficient coefficient) {
        return divide(ONE, coefficient);
    }

    public static DoubleCoefficient from(final double value) {
        return new DoubleCoefficient(value);
    }

    public static IntegerCoefficient from(final int value) {
        if (value == -1) {
            return negativeOne();
        }

        return new IntegerCoefficient(value);
    }

    public static IntegerCoefficient negativeOne() {
        return NEGATIVE_ONE;
    }

    public static Coefficient from(final int numerator, final int denominator) {
        return fromNumeratorAndDenominator(numerator, denominator);
    }

    public static ConstantCoefficient fromNumeratorAndDenominator(final int numerator, final int denominator) {
        if (numerator % denominator == 0) {
            return from(numerator / denominator);
        }

        return new RationalCoefficient(numerator, denominator);
    }

    public static double asDouble(final Coefficient coefficient) {
        if (coefficient instanceof IntegerCoefficient) {
            return ((IntegerCoefficient) coefficient).getValue();
        } else if (coefficient instanceof DoubleCoefficient) {
            return ((DoubleCoefficient) coefficient).getValue();
        } else if (coefficient instanceof RationalCoefficient) {
            final RationalCoefficient rationalCoefficient = (RationalCoefficient) coefficient;
            return (double) rationalCoefficient.getNumeratorValue() / rationalCoefficient.getDenominatorValue();
        } else if (coefficient instanceof LinearMCoefficient) {
            final LinearMCoefficient linearMCoefficient = (LinearMCoefficient) coefficient;
            if (Coefficients.isPositive(linearMCoefficient.getSlopeValue())) {
                return Double.POSITIVE_INFINITY;
            } else {
                return Double.NEGATIVE_INFINITY;
            }
        }

        throw new IllegalArgumentException("Invalid Input Coefficient");
    }

    public static boolean isPositive(final Coefficient coefficient) {
        return Coefficients.greaterThan(coefficient, Coefficients.ZERO);
    }

    public static boolean isZero(final Coefficient coefficient) {
        return Coefficients.equalTo(coefficient, Coefficients.ZERO);
    }

    public static boolean isNonNegative(final Coefficient coefficient) {
        return !Coefficients.isNegative(coefficient);
    }

    public static boolean isNegative(final Coefficient coefficient) {
        return Coefficients.lessThan(coefficient, Coefficients.ZERO);
    }

    public static boolean greaterThan(final Coefficient firstCoefficient, final Coefficient secondCoefficient) {
        return compare(firstCoefficient, secondCoefficient) > 0;
    }

    public static boolean equalTo(final Coefficient firstCoefficient, final Coefficient secondCoefficient) {
        return compare(firstCoefficient, secondCoefficient) == 0;
    }

    public static boolean lessThan(final Coefficient firstCoefficient, final Coefficient secondCoefficient) {
        return compare(firstCoefficient, secondCoefficient) < 0;
    }

    /*******************************************************************************************************************
     * Coefficients follow an ordering to see how to order comparisons and additions with the following priority:
     * 0: LinearMCoefficient
     * 1: RationalCoefficient
     * 2: DoubleCoefficient
     * 3: IntegerCoefficient
     ******************************************************************************************************************/

    /******************************************************************************************************************
     * COMPARATORS
     ******************************************************************************************************************/

    /**
     * The main entry point of comparisons between Coefficients.
     */
    public static int compare(final Coefficient firstCoefficient, final Coefficient secondCoefficient) {
        final Class<? extends Coefficient> firstClazz = firstCoefficient.getClass();
        final Class<? extends Coefficient> secondClazz = secondCoefficient.getClass();
        final int firstPriority = PRIORITY_MAP.get(firstClazz);
        final int secondPriority = PRIORITY_MAP.get(secondClazz);

        if (firstPriority <= secondPriority) {
            return compare(firstCoefficient, firstClazz, secondCoefficient, secondClazz);
        } else {
            return -compare(secondCoefficient, secondClazz, firstCoefficient, firstClazz);
        }
    }

    private static int compare(final Coefficient firstCoefficient,
                               final Class<? extends Coefficient> firstClazz,
                               final Coefficient secondCoefficient,
                               final Class<? extends Coefficient> secondClazz) {
        if (firstClazz == LinearMCoefficient.class) {
            if (secondClazz == LinearMCoefficient.class) {
                return compareLinearMtoLinearM(
                        (LinearMCoefficient) firstCoefficient,
                        (LinearMCoefficient) secondCoefficient
                );
            } else if (secondClazz == RationalCoefficient.class) {
                return compareLinearMToRational(
                        (LinearMCoefficient) firstCoefficient,
                        (RationalCoefficient) secondCoefficient
                );
            } else if (secondClazz == DoubleCoefficient.class) {
                return compareLinearMToDouble(
                        (LinearMCoefficient) firstCoefficient,
                        (DoubleCoefficient) secondCoefficient
                );
            } else if (secondClazz == IntegerCoefficient.class) {
                return compareLinearMToInteger(
                        (LinearMCoefficient) firstCoefficient,
                        (IntegerCoefficient) secondCoefficient
                );
            }
        } else if (firstClazz == RationalCoefficient.class) {
            if (secondClazz == RationalCoefficient.class) {
                return compareRationalToRational(
                        (RationalCoefficient) firstCoefficient,
                        (RationalCoefficient) secondCoefficient
                );
            } else if (secondClazz == DoubleCoefficient.class) {
                return compareRationalToDouble(
                        (RationalCoefficient) firstCoefficient,
                        (DoubleCoefficient) secondCoefficient
                );
            } else if (secondClazz == IntegerCoefficient.class) {
                return compareRationalToInteger(
                        (RationalCoefficient) firstCoefficient,
                        (IntegerCoefficient) secondCoefficient
                );
            }
        } else if (firstClazz == DoubleCoefficient.class) {
            if (secondClazz == DoubleCoefficient.class) {
                return compareDoubleToDouble(
                        (DoubleCoefficient) firstCoefficient,
                        (DoubleCoefficient) secondCoefficient
                );
            } else if (secondClazz == IntegerCoefficient.class) {
                return compareDoubleToInteger(
                        (DoubleCoefficient) firstCoefficient,
                        (IntegerCoefficient) secondCoefficient
                );
            }
        } else if (firstClazz == IntegerCoefficient.class) {
            if (secondClazz == IntegerCoefficient.class) {
                return compareIntegerToInteger(
                        (IntegerCoefficient) firstCoefficient,
                        (IntegerCoefficient) secondCoefficient
                );
            }
        }


        throw new IllegalArgumentException(
                String.format(
                        "Illegal Coefficients to compare: %s, %s",
                        firstCoefficient.toString(),
                        secondCoefficient.toString()));
    }

    private static int compareLinearMtoLinearM(final LinearMCoefficient firstCoefficient,
                                               final LinearMCoefficient secondCoefficient) {
        // First compare slope values
        final int compareValue
                = Coefficients.compare(firstCoefficient.getSlopeValue(), secondCoefficient.getSlopeValue());

        if (compareValue != 0) {
            return compareValue;
        }

        return Coefficients.compare(
                firstCoefficient.getInterceptValue(), secondCoefficient.getInterceptValue());
    }

    private static int compareLinearMToRational(final LinearMCoefficient firstCoefficient,
                                                final RationalCoefficient secondCoefficient) {
        if (Coefficients.isPositive(firstCoefficient.getSlopeValue())) {
            return Double.compare(Double.POSITIVE_INFINITY, secondCoefficient.getNumeratorValue());
        } else {
            return Double.compare(Double.NEGATIVE_INFINITY, secondCoefficient.getNumeratorValue());
        }
    }

    private static int compareLinearMToDouble(final LinearMCoefficient firstCoefficient,
                                              final DoubleCoefficient secondCoefficient) {
        if (Coefficients.isPositive(firstCoefficient.getSlopeValue())) {
            return Double.compare(Double.POSITIVE_INFINITY, secondCoefficient.getValue());
        } else {
            return Double.compare(Double.NEGATIVE_INFINITY, secondCoefficient.getValue());
        }
    }

    private static int compareLinearMToInteger(final LinearMCoefficient firstCoefficient,
                                               final IntegerCoefficient secondCoefficient) {
        if (Coefficients.isPositive(firstCoefficient.getSlopeValue())) {
            return Double.compare(Double.POSITIVE_INFINITY, secondCoefficient.getValue());
        } else {
            return Double.compare(Double.NEGATIVE_INFINITY, secondCoefficient.getValue());
        }
    }

    private static int compareRationalToRational(final RationalCoefficient firstCoefficient,
                                                 final RationalCoefficient secondCoefficient) {
        return Double.compare(
                (double) firstCoefficient.getNumeratorValue() / firstCoefficient.getDenominatorValue(),
                (double) secondCoefficient.getNumeratorValue() / secondCoefficient.getDenominatorValue()
        );
    }

    private static int compareRationalToDouble(final RationalCoefficient firstCoefficient,
                                               final DoubleCoefficient secondCoefficient) {
        return Double.compare(
                (double) firstCoefficient.getNumeratorValue() / firstCoefficient.getDenominatorValue(),
                secondCoefficient.getValue()
        );
    }

    private static int compareRationalToInteger(final RationalCoefficient firstCoefficient,
                                                final IntegerCoefficient secondCoefficient) {
        return Double.compare(
                (double) firstCoefficient.getNumeratorValue() / firstCoefficient.getDenominatorValue(),
                secondCoefficient.getValue()
        );
    }

    private static int compareDoubleToDouble(final DoubleCoefficient firstCoefficient,
                                             final DoubleCoefficient secondCoefficient) {
        return Double.compare(firstCoefficient.getValue(), secondCoefficient.getValue());
    }

    private static int compareDoubleToInteger(final DoubleCoefficient firstCoefficient,
                                              final IntegerCoefficient secondCoefficient) {
        return Double.compare(firstCoefficient.getValue(), secondCoefficient.getValue());
    }

    private static int compareIntegerToInteger(final IntegerCoefficient firstCoefficient,
                                               final IntegerCoefficient secondCoefficient) {
        return Integer.compare(firstCoefficient.getValue(), secondCoefficient.getValue());
    }

    /******************************************************************************************************************
     * ADDERS
     ******************************************************************************************************************/

    /**
     * The main entry point of additions between Coefficients.
     */
    public static Coefficient add(final Coefficient firstCoefficient, final Coefficient secondCoefficient) {
        final Class<? extends Coefficient> firstClazz = firstCoefficient.getClass();
        final Class<? extends Coefficient> secondClazz = secondCoefficient.getClass();
        final int firstPriority = PRIORITY_MAP.get(firstClazz);
        final int secondPriority = PRIORITY_MAP.get(secondClazz);

        if (firstPriority <= secondPriority) {
            return add(firstCoefficient, firstClazz, secondCoefficient, secondClazz);
        } else {
            return add(secondCoefficient, secondClazz, firstCoefficient, firstClazz);
        }
    }

    private static Coefficient add(final Coefficient firstCoefficient,
                                   final Class<? extends Coefficient> firstClazz,
                                   final Coefficient secondCoefficient,
                                   final Class<? extends Coefficient> secondClazz) {
        if (firstClazz == LinearMCoefficient.class) {
            if (secondClazz == LinearMCoefficient.class) {
                return addLinearMtoLinearM(
                        (LinearMCoefficient) firstCoefficient,
                        (LinearMCoefficient) secondCoefficient
                );
            } else if (secondClazz == RationalCoefficient.class) {
                return addLinearMToRational(
                        (LinearMCoefficient) firstCoefficient,
                        (RationalCoefficient) secondCoefficient
                );
            } else if (secondClazz == DoubleCoefficient.class) {
                return addLinearMToDouble(
                        (LinearMCoefficient) firstCoefficient,
                        (DoubleCoefficient) secondCoefficient
                );
            } else if (secondClazz == IntegerCoefficient.class) {
                return addLinearMToInteger(
                        (LinearMCoefficient) firstCoefficient,
                        (IntegerCoefficient) secondCoefficient
                );
            }
        } else if (firstClazz == RationalCoefficient.class) {
            if (secondClazz == RationalCoefficient.class) {
                return addRationalToRational(
                        (RationalCoefficient) firstCoefficient,
                        (RationalCoefficient) secondCoefficient
                );
            } else if (secondClazz == DoubleCoefficient.class) {
                return addRationalToDouble(
                        (RationalCoefficient) firstCoefficient,
                        (DoubleCoefficient) secondCoefficient
                );
            } else if (secondClazz == IntegerCoefficient.class) {
                return addRationalToInteger(
                        (RationalCoefficient) firstCoefficient,
                        (IntegerCoefficient) secondCoefficient
                );
            }
        } else if (firstClazz == DoubleCoefficient.class) {
            if (secondClazz == DoubleCoefficient.class) {
                return addDoubleToDouble(
                        (DoubleCoefficient) firstCoefficient,
                        (DoubleCoefficient) secondCoefficient
                );
            } else if (secondClazz == IntegerCoefficient.class) {
                return addDoubleToInteger(
                        (DoubleCoefficient) firstCoefficient,
                        (IntegerCoefficient) secondCoefficient
                );
            }
        } else if (firstClazz == IntegerCoefficient.class) {
            if (secondClazz == IntegerCoefficient.class) {
                return addIntegerToInteger(
                        (IntegerCoefficient) firstCoefficient,
                        (IntegerCoefficient) secondCoefficient
                );
            }
        }


        throw new IllegalArgumentException(
                String.format(
                        "Illegal Coefficients to add: %s, %s",
                        firstCoefficient.toString(),
                        secondCoefficient.toString()));
    }

    private static Coefficient addLinearMtoLinearM(final LinearMCoefficient firstCoefficient,
                                                   final LinearMCoefficient secondCoefficient) {
        final ConstantCoefficient newSlopeValue = (ConstantCoefficient) Coefficients.add(
                firstCoefficient.getSlopeValue(), secondCoefficient.getSlopeValue()
        );
        final ConstantCoefficient newInterceptValue = (ConstantCoefficient) Coefficients.add(
                firstCoefficient.getInterceptValue(), secondCoefficient.getInterceptValue()
        );

        if (Coefficients.isZero(newSlopeValue)) {
            return newInterceptValue;
        }

        return new LinearMCoefficient(newSlopeValue, newInterceptValue);
    }

    private static Coefficient addLinearMToRational(final LinearMCoefficient firstCoefficient,
                                                    final RationalCoefficient secondCoefficient) {
        return new LinearMCoefficient(
                firstCoefficient.getSlopeValue(),
                (ConstantCoefficient) Coefficients.add(firstCoefficient.getInterceptValue(), secondCoefficient)
        );
    }

    private static Coefficient addLinearMToDouble(final LinearMCoefficient firstCoefficient,
                                                  final DoubleCoefficient secondCoefficient) {
        return new LinearMCoefficient(
                firstCoefficient.getSlopeValue(),
                (ConstantCoefficient) Coefficients.add(firstCoefficient.getInterceptValue(), secondCoefficient)
        );
    }

    private static Coefficient addLinearMToInteger(final LinearMCoefficient firstCoefficient,
                                                   final IntegerCoefficient secondCoefficient) {
        return new LinearMCoefficient(
                firstCoefficient.getSlopeValue(),
                (ConstantCoefficient) Coefficients.add(firstCoefficient.getInterceptValue(), secondCoefficient)
        );
    }

    private static Coefficient addRationalToRational(final RationalCoefficient firstCoefficient,
                                                     final RationalCoefficient secondCoefficient) {
        final int a = firstCoefficient.getNumeratorValue();
        final int b = firstCoefficient.getDenominatorValue();
        final int c = secondCoefficient.getNumeratorValue();
        final int d = secondCoefficient.getDenominatorValue();

        return Coefficients.fromNumeratorAndDenominator(
                a * d + b * c,
                b * d
        );
    }

    private static Coefficient addRationalToDouble(final RationalCoefficient firstCoefficient,
                                                   final DoubleCoefficient secondCoefficient) {
        final double n = firstCoefficient.getNumeratorValue();
        final double d = firstCoefficient.getDenominatorValue();
        final double v = secondCoefficient.getValue();

        return new DoubleCoefficient((n + d * v) / d);
    }

    private static Coefficient addRationalToInteger(final RationalCoefficient firstCoefficient,
                                                    final IntegerCoefficient secondCoefficient) {
        return Coefficients.fromNumeratorAndDenominator(
                secondCoefficient.getValue() * firstCoefficient.getDenominatorValue()
                        + firstCoefficient.getNumeratorValue(),
                firstCoefficient.getDenominatorValue()
        );
    }

    private static Coefficient addDoubleToDouble(final DoubleCoefficient firstCoefficient,
                                                 final DoubleCoefficient secondCoefficient) {
        return new DoubleCoefficient(firstCoefficient.getValue() + secondCoefficient.getValue());
    }

    private static Coefficient addDoubleToInteger(final DoubleCoefficient firstCoefficient,
                                                  final IntegerCoefficient secondCoefficient) {
        return new DoubleCoefficient(firstCoefficient.getValue() + secondCoefficient.getValue());
    }

    private static Coefficient addIntegerToInteger(final IntegerCoefficient firstCoefficient,
                                                   final IntegerCoefficient secondCoefficient) {
        return new IntegerCoefficient(firstCoefficient.getValue() + secondCoefficient.getValue());
    }

    /******************************************************************************************************************
     * SCALERS
     ******************************************************************************************************************/

    /**
     * The main entry point of scaling between Coefficients.
     */
    public static Coefficient scaleBy(final Coefficient firstCoefficient,
                                      final Coefficient secondCoefficient) {
        final Class<? extends Coefficient> firstClazz = firstCoefficient.getClass();
        final Class<? extends Coefficient> secondClazz = secondCoefficient.getClass();
        final int firstPriority = PRIORITY_MAP.get(firstClazz);
        final int secondPriority = PRIORITY_MAP.get(secondClazz);

        if (firstPriority <= secondPriority) {
            return scaleBy(firstCoefficient, firstClazz, secondCoefficient, secondClazz);
        } else {
            return scaleBy(secondCoefficient, secondClazz, firstCoefficient, firstClazz);
        }
    }

    private static Coefficient scaleBy(final Coefficient firstCoefficient,
                                       final Class<? extends Coefficient> firstClazz,
                                       final Coefficient secondCoefficient,
                                       final Class<? extends Coefficient> secondClazz) {
        if (firstClazz == LinearMCoefficient.class) {
            if (secondClazz == LinearMCoefficient.class) {
                throw new IllegalArgumentException("Can't scale a linear function by another!");
            } else if (secondClazz == RationalCoefficient.class) {
                return scaleLinearMByRational(
                        (LinearMCoefficient) firstCoefficient,
                        (RationalCoefficient) secondCoefficient
                );
            } else if (secondClazz == DoubleCoefficient.class) {
                return scaleLinearMByDouble(
                        (LinearMCoefficient) firstCoefficient,
                        (DoubleCoefficient) secondCoefficient
                );
            } else if (secondClazz == IntegerCoefficient.class) {
                return scaleLinearMByInteger(
                        (LinearMCoefficient) firstCoefficient,
                        (IntegerCoefficient) secondCoefficient
                );
            }
        } else if (firstClazz == RationalCoefficient.class) {
            if (secondClazz == RationalCoefficient.class) {
                return scaleRationalByRational(
                        (RationalCoefficient) firstCoefficient,
                        (RationalCoefficient) secondCoefficient
                );
            } else if (secondClazz == DoubleCoefficient.class) {
                return scaleRationalByDouble(
                        (RationalCoefficient) firstCoefficient,
                        (DoubleCoefficient) secondCoefficient
                );
            } else if (secondClazz == IntegerCoefficient.class) {
                return scaleRationalByInteger(
                        (RationalCoefficient) firstCoefficient,
                        (IntegerCoefficient) secondCoefficient
                );
            }
        } else if (firstClazz == DoubleCoefficient.class) {
            if (secondClazz == DoubleCoefficient.class) {
                return scaleDoubleByDouble(
                        (DoubleCoefficient) firstCoefficient,
                        (DoubleCoefficient) secondCoefficient
                );
            } else if (secondClazz == IntegerCoefficient.class) {
                return scaleDoubleByInteger(
                        (DoubleCoefficient) firstCoefficient,
                        (IntegerCoefficient) secondCoefficient
                );
            }
        } else if (firstClazz == IntegerCoefficient.class) {
            if (secondClazz == IntegerCoefficient.class) {
                return scaleIntegerByInteger(
                        (IntegerCoefficient) firstCoefficient,
                        (IntegerCoefficient) secondCoefficient
                );
            }
        }


        throw new IllegalArgumentException(
                String.format(
                        "Illegal Coefficients to scale: %s, %s",
                        firstCoefficient.toString(),
                        secondCoefficient.toString()));
    }

    private static Coefficient scaleLinearMByRational(final LinearMCoefficient firstCoefficient,
                                                      final RationalCoefficient secondCoefficient) {
        return new LinearMCoefficient(
                (ConstantCoefficient) Coefficients.scaleBy(firstCoefficient.getSlopeValue(), secondCoefficient),
                (ConstantCoefficient) Coefficients.scaleBy(firstCoefficient.getInterceptValue(), secondCoefficient)
        );
    }

    private static Coefficient scaleLinearMByDouble(final LinearMCoefficient firstCoefficient,
                                                    final DoubleCoefficient secondCoefficient) {
        return new LinearMCoefficient(
                (ConstantCoefficient) Coefficients.scaleBy(firstCoefficient.getSlopeValue(), secondCoefficient),
                (ConstantCoefficient) Coefficients.scaleBy(firstCoefficient.getInterceptValue(), secondCoefficient)
        );
    }

    private static Coefficient scaleLinearMByInteger(final LinearMCoefficient firstCoefficient,
                                                     final IntegerCoefficient secondCoefficient) {
        if (Coefficients.isZero(secondCoefficient)) {
            return Coefficients.ZERO;
        }

        return new LinearMCoefficient(
                (ConstantCoefficient) Coefficients.scaleBy(firstCoefficient.getSlopeValue(), secondCoefficient),
                (ConstantCoefficient) Coefficients.scaleBy(firstCoefficient.getInterceptValue(), secondCoefficient)
        );
    }

    private static Coefficient scaleRationalByRational(final RationalCoefficient firstCoefficient,
                                                       final RationalCoefficient secondCoefficient) {
        final int a = firstCoefficient.getNumeratorValue();
        final int b = firstCoefficient.getDenominatorValue();
        final int c = secondCoefficient.getNumeratorValue();
        final int d = secondCoefficient.getDenominatorValue();

        return Coefficients.fromNumeratorAndDenominator(
                a * c,
                b * d
        );
    }

    private static Coefficient scaleRationalByDouble(final RationalCoefficient firstCoefficient,
                                                     final DoubleCoefficient secondCoefficient) {

        return new DoubleCoefficient(
                firstCoefficient.getNumeratorValue() * secondCoefficient.getValue()
                        / firstCoefficient.getDenominatorValue()
        );
    }

    private static Coefficient scaleRationalByInteger(final RationalCoefficient firstCoefficient,
                                                      final IntegerCoefficient secondCoefficient) {
        return Coefficients.fromNumeratorAndDenominator(
                firstCoefficient.getNumeratorValue() * secondCoefficient.getValue(),
                firstCoefficient.getDenominatorValue()
        );
    }

    private static Coefficient scaleDoubleByDouble(final DoubleCoefficient firstCoefficient,
                                                   final DoubleCoefficient secondCoefficient) {
        return new DoubleCoefficient(firstCoefficient.getValue() * secondCoefficient.getValue());
    }

    private static Coefficient scaleDoubleByInteger(final DoubleCoefficient firstCoefficient,
                                                    final IntegerCoefficient secondCoefficient) {
        return new DoubleCoefficient(firstCoefficient.getValue() * secondCoefficient.getValue());
    }

    private static Coefficient scaleIntegerByInteger(final IntegerCoefficient firstCoefficient,
                                                     final IntegerCoefficient secondCoefficient) {
        return new IntegerCoefficient(firstCoefficient.getValue() * secondCoefficient.getValue());
    }
}
