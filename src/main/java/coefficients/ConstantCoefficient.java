package coefficients;

public interface ConstantCoefficient extends Coefficient {
    ConstantCoefficient floor();

    ConstantCoefficient ceil();

    @Override
    ConstantCoefficient negate();
}
