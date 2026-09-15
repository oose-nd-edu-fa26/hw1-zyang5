package edu.nd.oose.hw1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class JeffersonApportionment
        implements ApportionmentMethod {

    private static final int MAX_SEARCH_ITERATIONS = 10_000;

    @Override
    public Map<State, Integer> apportion(
            List<State> states,
            int representativeCount
    ) {
        validateInputs(states, representativeCount);

        long totalPopulation = getTotalPopulation(states);

        if (totalPopulation == 0) {
            throw new IllegalArgumentException(
                    "The total population must be greater than zero."
            );
        }

        Optional<Map<State, Integer>> divisorResult =
                findApportionmentUsingDivisor(
                        states,
                        representativeCount,
                        totalPopulation
                );

        return divisorResult.orElseGet(
                () -> allocateUsingHighestAverages(
                        states,
                        representativeCount
                )
        );
    }

    private static void validateInputs(
            List<State> states,
            int representativeCount
    ) {
        if (states == null || states.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one state is required for apportionment."
            );
        }

        if (representativeCount <= 0) {
            throw new IllegalArgumentException(
                    "The number of representatives must be greater than zero."
            );
        }

        for (State state : states) {
            if (state == null) {
                throw new IllegalArgumentException(
                        "The state list cannot contain null values."
                );
            }
        }
    }

    private static long getTotalPopulation(List<State> states) {
        long totalPopulation = 0;

        for (State state : states) {
            totalPopulation += state.getPopulation();
        }

        return totalPopulation;
    }

    private static Optional<Map<State, Integer>>
    findApportionmentUsingDivisor(
            List<State> states,
            int representativeCount,
            long totalPopulation
    ) {
        double initialDivisor =
                totalPopulation / (double) representativeCount;

        Map<State, Integer> initialApportionment =
                calculateApportionment(states, initialDivisor);

        if (getAllocatedRepresentativeCount(initialApportionment)
                == representativeCount) {
            return Optional.of(initialApportionment);
        }

        double lowerBound = 0.0;
        double upperBound = initialDivisor;

        for (int iteration = 0;
             iteration < MAX_SEARCH_ITERATIONS;
             iteration++) {

            double divisor =
                    (lowerBound + upperBound) / 2.0;

            if (divisor == lowerBound || divisor == upperBound) {
                break;
            }

            Map<State, Integer> apportionment =
                    calculateApportionment(states, divisor);

            long allocatedRepresentatives =
                    getAllocatedRepresentativeCount(apportionment);

            if (allocatedRepresentatives == representativeCount) {
                return Optional.of(apportionment);
            }

            if (allocatedRepresentatives
                    < representativeCount) {
                upperBound = divisor;
            } else {
                lowerBound = divisor;
            }
        }

        return Optional.empty();
    }

    private static Map<State, Integer> calculateApportionment(
            List<State> states,
            double divisor
    ) {
        Map<State, Integer> apportionment = new HashMap<>();

        for (State state : states) {
            double quota =
                    state.getPopulation() / divisor;

            int representatives =
                    (int) Math.floor(quota);

            apportionment.put(state, representatives);
        }

        return apportionment;
    }

    private static long getAllocatedRepresentativeCount(
            Map<State, Integer> apportionment
    ) {
        long total = 0;

        for (int representatives : apportionment.values()) {
            total += representatives;
        }

        return total;
    }

    private static Map<State, Integer>
    allocateUsingHighestAverages(
            List<State> states,
            int representativeCount
    ) {
        Map<State, Integer> apportionment = new HashMap<>();

        for (State state : states) {
            apportionment.put(state, 0);
        }

        for (int seat = 0;
             seat < representativeCount;
             seat++) {

            State selectedState = findHighestPriorityState(
                    states,
                    apportionment
            );

            int currentRepresentatives =
                    apportionment.get(selectedState);

            apportionment.put(
                    selectedState,
                    currentRepresentatives + 1
            );
        }

        return apportionment;
    }

    private static State findHighestPriorityState(
            List<State> states,
            Map<State, Integer> apportionment
    ) {
        State selectedState = null;
        double highestPriority = -1.0;

        for (State state : states) {
            int currentRepresentatives =
                    apportionment.get(state);

            double priority =
                    state.getPopulation()
                            / (currentRepresentatives + 1.0);

            if (selectedState == null
                    || priority > highestPriority
                    || (Double.compare(
                            priority,
                            highestPriority
                    ) == 0
                    && state.getName().compareTo(
                            selectedState.getName()
                    ) < 0)) {

                selectedState = state;
                highestPriority = priority;
            }
        }

        return selectedState;
    }
}
