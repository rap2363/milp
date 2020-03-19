package coefficients;

public interface ConstantCoefficient extends Coefficient {
    ConstantCoefficient inverse();

    ConstantCoefficient floor();

    ConstantCoefficient ceil();

    ConstantCoefficient multiply(ConstantCoefficient other);

    ConstantCoefficient negate();
}
