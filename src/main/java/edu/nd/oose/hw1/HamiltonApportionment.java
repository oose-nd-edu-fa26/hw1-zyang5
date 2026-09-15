package edu.nd.oose.hw1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class HamiltonApportionment implements ApportionmentMethod {

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

        double divisor = getDivisor(
                totalPopulation,
                representativeCount
        );

        Map<State, Integer> apportionment =
                getRoundedDownApportionment(states, divisor);

        allocateRemainingRepresentatives(
                states,
                apportionment,
                divisor,
                representativeCount
        );

        return apportionment;
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
    }

    private static long getTotalPopulation(List<State> states) {
        long totalPopulation = 0;

        for (State state : states) {
            totalPopulation += state.getPopulation();
        }

        return totalPopulation;
    }

    private static double getDivisor(
            long totalPopulation,
            int representativeCount
    ) {
        return totalPopulation / (double) representativeCount;
    }

    private static Map<State, Integer> getRoundedDownApportionment(
            List<State> states,
            double divisor
    ) {
        Map<State, Integer> apportionment = new HashMap<>();

        for (State state : states) {
            double quota = state.getPopulation() / divisor;
            int roundedDownRepresentatives =
                    (int) Math.floor(quota);

            apportionment.put(
                    state,
                    roundedDownRepresentatives
            );
        }

        return apportionment;
    }

    private static void allocateRemainingRepresentatives(
            List<State> states,
            Map<State, Integer> apportionment,
            double divisor,
            int representativeCount
    ) {
        int allocatedRepresentatives =
                getAllocatedRepresentativeCount(apportionment);

        int remainingRepresentatives =
                representativeCount - allocatedRepresentatives;

        List<State> statesByRemainder =
                getStatesByDescendingRemainder(states, divisor);

        for (int index = 0;
             index < remainingRepresentatives;
             index++) {

            State state = statesByRemainder.get(index);
            int currentRepresentatives =
                    apportionment.get(state);

            apportionment.put(
                    state,
                    currentRepresentatives + 1
            );
        }
    }

    private static int getAllocatedRepresentativeCount(
            Map<State, Integer> apportionment
    ) {
        int allocatedRepresentatives = 0;

        for (int representatives : apportionment.values()) {
            allocatedRepresentatives += representatives;
        }

        return allocatedRepresentatives;
    }

    private static List<State> getStatesByDescendingRemainder(
            List<State> states,
            double divisor
    ) {
        List<State> sortedStates = new ArrayList<>(states);

        sortedStates.sort(
                Comparator.comparingDouble(
                        (State state) ->
                                getRemainder(state, divisor)
                ).reversed().thenComparing(State::getName)
        );

        return sortedStates;
    }

    private static double getRemainder(
            State state,
            double divisor
    ) {
        double quota = state.getPopulation() / divisor;
        return quota - Math.floor(quota);
    }
}