package lang;

/**
 * Basic copy of Guava's Preconditions.
 */
public final class Preconditions {
    private Preconditions() {
        // Exists to defeat instantiation.
    }

    public static void checkArgument(final boolean condition) {
        checkArgument(condition, "");
    }

    public static void checkArgument(final boolean condition, final String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void checkNotNull(final Object obj) {
        checkNotNull(obj, "");
    }

    public static void checkNotNull(final Object obj, final String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
