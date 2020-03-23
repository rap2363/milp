package solver;

import math.Vector;

import java.util.Optional;

public interface Solver {
    Optional<Vector> getOptimalSolutionIfFeasible();

    double getOptimalValue();
}
