package edu.nd.oose.hw1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public final class CsvApportionmentFormat
        implements ApportionmentFormat {

    @Override
    public String format(
            List<State> states,
            Map<State, Integer> apportionment
    ) {
        List<State> statesAlphabetically =
                new ArrayList<>(states);

        statesAlphabetically.sort(
                Comparator.comparing(State::getName)
        );

        StringBuilder output = new StringBuilder();

        output.append("State,Representatives")
                .append(System.lineSeparator());

        for (State state : statesAlphabetically) {
            output.append(state.getName())
                    .append(",")
                    .append(apportionment.get(state))
                    .append(System.lineSeparator());
        }

        return output.toString();
    }
}
