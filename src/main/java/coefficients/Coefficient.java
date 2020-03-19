package coefficients;

public interface Coefficient extends Comparable<Coefficient> {
    Coefficient plus(Coefficient other);

    Coefficient negate();
}
