package coefficients;

public interface Coefficient {
    Coefficient inverse();

    Coefficient floor();

    Coefficient ceil();
}
