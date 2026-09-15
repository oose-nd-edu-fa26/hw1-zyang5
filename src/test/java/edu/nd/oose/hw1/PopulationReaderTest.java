package edu.nd.oose.hw1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PopulationReaderTest {

    @TempDir
    Path temporaryDirectory;

    @Test
    void readTrimsFieldsAndIgnoresExtraColumns() throws IOException {
        Path file = temporaryDirectory.resolve("population.csv");
        Files.writeString(file, """
                State,Population,Notes
                 Beta , 300 ,extra column
                Alpha,100
                """);

        List<State> states = PopulationReader.read(file.toString());

        assertEquals(2, states.size());
        assertEquals("Beta", states.get(0).getName());
        assertEquals(300L, states.get(0).getPopulation());
        assertEquals("Alpha", states.get(1).getName());
        assertEquals(100L, states.get(1).getPopulation());
    }

    @Test
    void readSkipsInvalidRowsAndContinues() throws IOException {
        Path file = temporaryDirectory.resolve("population.csv");
        Files.writeString(file, """
                State,Population
                Valid State,500
                Negative State,-10
                Text State,abc
                Missing Population
                """);

        List<State> states = PopulationReader.read(file.toString());

        assertEquals(1, states.size());
        assertEquals("Valid State", states.get(0).getName());
        assertEquals(500L, states.get(0).getPopulation());
    }

    @Test
    void readRejectsFileWithNoValidRecords() throws IOException {
        Path file = temporaryDirectory.resolve("population.csv");
        Files.writeString(file, """
                State,Population
                Broken State,not-a-number
                Missing Population
                """);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> PopulationReader.read(file.toString())
        );

        assertTrue(exception.getMessage().contains("no valid"));
    }

    @Test
    void readRejectsMissingFile() {
        Path file = temporaryDirectory.resolve("missing.csv");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> PopulationReader.read(file.toString())
        );

        assertTrue(exception.getMessage().contains("does not exist"));
    }

    @Test
    void readRejectsHeaderWithoutState() throws IOException {
        Path file = temporaryDirectory.resolve("missing_state.csv");
        Files.writeString(file, """
            ID,Population,Capital
            1,100,Alpha City
            """);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> PopulationReader.read(file.toString())
        );

        assertTrue(exception.getMessage().contains("State"));
        assertTrue(exception.getMessage().contains("Population"));
    }

    @Test
    void readRejectsHeaderWithoutPopulation() throws IOException {
        Path file = temporaryDirectory.resolve("missing_population.csv");
        Files.writeString(file, """
            ID,State,Capital
            1,Alpha,Alpha City
            """);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> PopulationReader.read(file.toString())
        );

        assertTrue(exception.getMessage().contains("State"));
        assertTrue(exception.getMessage().contains("Population"));
    }
}