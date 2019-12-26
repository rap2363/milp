package coefficients;

public interface Coefficient extends Comparable<Coefficient> {
    Coefficient inverse();

    Coefficient floor();

    Coefficient ceil();

    Coefficient plus(Coefficient other);

    Coefficient multiply(Coefficient other);
}
