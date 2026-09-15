package edu.nd.oose.hw1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public final class Main {

    private Main() {
        // Prevent creation of Main objects.
    }

    public static void main(String[] args) {
        Configuration configuration =
                Arguments.parse(args);

        List<State> states = PopulationReader.read(
                configuration.getInputFilename()
        );

        ApportionmentMethod method =
                configuration.getApportionmentMethod();

        Map<State, Integer> apportionment =
                method.apportion(
                        states,
                        configuration.getRepresentativeCount()
                );

        configuration.getOutputFilename().ifPresent(
                filename -> writeCsvOutput(
                        filename,
                        states,
                        apportionment
                )
        );

        ApportionmentFormat consoleFormat =
                new ConsoleApportionmentFormat();

        System.out.print(
                consoleFormat.format(states, apportionment)
        );
    }

    private static void writeCsvOutput(
            String filename,
            List<State> states,
            Map<State, Integer> apportionment
    ) {
        ApportionmentFormat csvFormat =
                new CsvApportionmentFormat();

        String csvOutput =
                csvFormat.format(states, apportionment);

        try {
            Files.writeString(
                    Path.of(filename),
                    csvOutput
            );
        } catch (IOException exception) {
            throw new IllegalArgumentException(
                    "Unable to write output file: "
                            + filename
                            + ". Check the file permissions "
                            + "and try again.",
                    exception
            );
        }
    }
}