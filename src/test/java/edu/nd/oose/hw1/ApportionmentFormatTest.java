package edu.nd.oose.hw1;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApportionmentFormatTest {

    @Test
    void csvFormatIncludesHeaderAndSortsStatesAlphabetically() {
        State beta = new State("Beta", 300);
        State alpha = new State("Alpha", 100);

        List<State> states = List.of(beta, alpha);
        Map<State, Integer> apportionment = Map.of(
                beta, 2,
                alpha, 1
        );

        ApportionmentFormat format =
                new CsvApportionmentFormat();

        String expected = String.join(
                System.lineSeparator(),
                "State,Representatives",
                "Alpha,1",
                "Beta,2",
                ""
        );

        assertEquals(
                expected,
                format.format(states, apportionment)
        );
    }

    @Test
    void consoleFormatSortsStatesAlphabetically() {
        State beta = new State("Beta", 300);
        State alpha = new State("Alpha", 100);

        List<State> states = List.of(beta, alpha);
        Map<State, Integer> apportionment = Map.of(
                beta, 2,
                alpha, 1
        );

        ApportionmentFormat format =
                new ConsoleApportionmentFormat();

        String expected = String.join(
                System.lineSeparator(),
                "Alpha - 1",
                "Beta - 2",
                ""
        );

        assertEquals(
                expected,
                format.format(states, apportionment)
        );
    }
}
