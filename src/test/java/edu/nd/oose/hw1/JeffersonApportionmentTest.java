package edu.nd.oose.hw1;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JeffersonApportionmentTest {

    private final ApportionmentMethod method =
            new JeffersonApportionment();

    @Test
    void apportionReturnsExpectedResultsForProfessorExample() {
        State delaware = new State("Delaware", 989948);
        State maryland = new State("Maryland", 6177224);
        State pennsylvania = new State("Pennsylvania", 13002700);
        State virginia = new State("Virginia", 8631393);
        State westVirginia = new State("West Virginia", 1793716);

        List<State> states = List.of(
                delaware,
                maryland,
                pennsylvania,
                virginia,
                westVirginia
        );

        Map<State, Integer> result =
                method.apportion(states, 25);

        assertEquals(0, result.get(delaware));
        assertEquals(5, result.get(maryland));
        assertEquals(12, result.get(pennsylvania));
        assertEquals(7, result.get(virginia));
        assertEquals(1, result.get(westVirginia));
    }

    @Test
    void apportionAllowsStatesToReceiveZeroRepresentatives() {
        State alpha = new State("Alpha", 60);
        State beta = new State("Beta", 30);
        State gamma = new State("Gamma", 10);

        Map<State, Integer> result = method.apportion(
                List.of(alpha, beta, gamma),
                7
        );

        assertEquals(5, result.get(alpha));
        assertEquals(2, result.get(beta));
        assertEquals(0, result.get(gamma));

        int totalRepresentatives = result.values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();

        assertEquals(7, totalRepresentatives);
    }

    @Test
    void apportionRejectsNonpositiveRepresentativeCount() {
        List<State> states = List.of(
                new State("Alpha", 100)
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> method.apportion(states, 0)
        );

        assertEquals(
                "The number of representatives must be greater than zero.",
                exception.getMessage()
        );
    }

    @Test
    void apportionRejectsZeroTotalPopulation() {
        List<State> states = List.of(
                new State("Alpha", 0),
                new State("Beta", 0)
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> method.apportion(states, 10)
        );

        assertEquals(
                "The total population must be greater than zero.",
                exception.getMessage()
        );
    }
}