package edu.nd.oose.hw1;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class PopulationReader {

    private PopulationReader() {
        // Prevent creation of PopulationReader objects.
    }

    public static List<State> read(String filename) {
        Path filePath = validateInputPath(filename);
        List<State> states = readStates(filePath);

        if (states.isEmpty()) {
            throw new IllegalArgumentException(
                    "The input file contains no valid state and population records."
            );
        }

        return states;
    }

    private static Path validateInputPath(String filename) {
        if (filename == null || filename.isBlank()) {
            throw new IllegalArgumentException(
                    "Missing input filename. Provide the path to a population CSV file."
            );
        }

        Path filePath = Path.of(filename);

        if (!Files.exists(filePath)) {
            throw new IllegalArgumentException(
                    "Input file does not exist: " + filename
                            + ". Check the filename or file path and try again."
            );
        }

        if (!Files.isRegularFile(filePath) || !Files.isReadable(filePath)) {
            throw new IllegalArgumentException(
                    "Input file cannot be read: " + filename
                            + ". Check the file permissions and try again."
            );
        }

        return filePath;
    }

    private static List<State> readStates(Path filePath) {
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String headerLine = reader.readLine();
            int[] requiredColumns = findRequiredColumns(headerLine);

            return readDataLines(
                    reader,
                    requiredColumns[0],
                    requiredColumns[1]
            );
        } catch (IOException exception) {
            throw new IllegalArgumentException(
                    "Unable to read input file: " + filePath
                            + ". Check the file and try again.",
                    exception
            );
        }
    }

    private static int[] findRequiredColumns(String headerLine) {
        if (headerLine == null) {
            throw missingRequiredColumnsException();
        }

        String[] headings = headerLine.split(",", -1);
        int stateColumn = findColumn(headings, "State");
        int populationColumn = findColumn(headings, "Population");

        if (stateColumn == -1 || populationColumn == -1) {
            throw missingRequiredColumnsException();
        }

        return new int[]{stateColumn, populationColumn};
    }

    private static int findColumn(String[] headings, String requiredHeading) {
        for (int index = 0; index < headings.length; index++) {
            if (headings[index].trim().equalsIgnoreCase(requiredHeading)) {
                return index;
            }
        }

        return -1;
    }

    private static IllegalArgumentException missingRequiredColumnsException() {
        return new IllegalArgumentException(
                "The CSV header must contain both required headings: "
                        + "State and Population."
        );
    }

    private static List<State> readDataLines(
            BufferedReader reader,
            int stateColumn,
            int populationColumn
    ) throws IOException {
        List<State> states = new ArrayList<>();
        String line;
        int lineNumber = 1;

        while ((line = reader.readLine()) != null) {
            lineNumber++;
            State state = parseState(
                    lineNumber,
                    line,
                    stateColumn,
                    populationColumn
            );

            if (state != null) {
                states.add(state);
            }
        }

        return states;
    }

    private static State parseState(
            int lineNumber,
            String line,
            int stateColumn,
            int populationColumn
    ) {
        String[] columns = line.split(",", -1);
        int largestRequiredColumn = Math.max(stateColumn, populationColumn);

        if (columns.length <= largestRequiredColumn) {
            printWarning(lineNumber, line);
            return null;
        }

        String stateName = columns[stateColumn].trim();
        String populationText = columns[populationColumn].trim();

        try {
            if (stateName.isEmpty()) {
                throw new IllegalArgumentException();
            }

            long population = Long.parseLong(populationText);
            return new State(stateName, population);
        } catch (IllegalArgumentException exception) {
            printWarning(lineNumber, line);
            return null;
        }
    }

    private static void printWarning(int lineNumber, String line) {
        System.err.println(
                "Line " + lineNumber + " - Bad format - " + line
        );
    }
}