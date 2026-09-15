package edu.nd.oose.hw1;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArgumentsTest {

    @Test
    void parseUsesJeffersonAnd435ByDefault() {
        Configuration configuration =
                Arguments.parse(new String[]{"input.csv"});

        assertEquals(
                "input.csv",
                configuration.getInputFilename()
        );
        assertEquals(
                435,
                configuration.getRepresentativeCount()
        );
        assertInstanceOf(
                JeffersonApportionment.class,
                configuration.getApportionmentMethod()
        );
        assertTrue(
                configuration.getOutputFilename().isEmpty()
        );
    }

    @Test
    void parseUsesSpecifiedCountAndHamilton() {
        Configuration configuration = Arguments.parse(
                new String[]{
                        "input.csv",
                        "100",
                        "--hamilton"
                }
        );

        assertEquals(
                100,
                configuration.getRepresentativeCount()
        );
        assertInstanceOf(
                HamiltonApportionment.class,
                configuration.getApportionmentMethod()
        );
    }

    @Test
    void parseIgnoresCountWhenNotImmediatelyAfterInput() {
        Configuration configuration = Arguments.parse(
                new String[]{
                        "input.csv",
                        "--hamilton",
                        "100"
                }
        );

        assertEquals(
                435,
                configuration.getRepresentativeCount()
        );
        assertInstanceOf(
                HamiltonApportionment.class,
                configuration.getApportionmentMethod()
        );
    }

    @Test
    void parseAcceptsValidOutputFilename() {
        Configuration configuration = Arguments.parse(
                new String[]{
                        "input.csv",
                        "--out",
                        "result_2026.csv"
                }
        );

        assertEquals(
                Optional.of("result_2026.csv"),
                configuration.getOutputFilename()
        );
    }

    @Test
    void parseRejectsMissingOutputFilename() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Arguments.parse(
                        new String[]{
                                "input.csv",
                                "--out"
                        }
                )
        );

        assertTrue(
                exception.getMessage()
                        .contains("output filename")
        );
    }

    @Test
    void parseRejectsInvalidOutputFilenames() {
        String[] invalidFilenames = {
                "--hamilton",
                "results.txt",
                "my-results.csv",
                "results.final.csv",
                "folder/results.csv"
        };

        for (String filename : invalidFilenames) {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> Arguments.parse(
                            new String[]{
                                    "input.csv",
                                    "--out",
                                    filename
                            }
                    ),
                    "Expected invalid filename: " + filename
            );
        }
    }

    @Test
    void parseRejectsFlagBeforeInputFilename() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Arguments.parse(
                        new String[]{
                                "--hamilton",
                                "input.csv"
                        }
                )
        );

        assertTrue(
                exception.getMessage()
                        .contains("first argument")
        );
    }

    @Test
    void parseRejectsNonpositiveRepresentativeCount() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Arguments.parse(
                        new String[]{
                                "input.csv",
                                "-10"
                        }
                )
        );

        assertTrue(
                exception.getMessage()
                        .contains("greater than zero")
        );
    }
}
