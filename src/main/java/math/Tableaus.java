package math;

public final class Tableaus {
    private Tableaus() {
        // Exists to defeat instantiation
    }

    public static Tableau solve(final Tableau initial) {
        Tableau previous = null;
        Tableau current = initial;
        while (!current.equals(previous)) {
            previous = current;
            current = current.pivot();
        }

        return current;
    }
}
