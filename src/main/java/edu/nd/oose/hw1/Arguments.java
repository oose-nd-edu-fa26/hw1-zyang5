package edu.nd.oose.hw1;

import java.util.Optional;
import java.util.regex.Pattern;

public final class Arguments {

    private static final int DEFAULT_REPRESENTATIVE_COUNT = 435;

    private static final Pattern OUTPUT_FILENAME_PATTERN =
            Pattern.compile("[A-Za-z0-9_]+\\.csv");

    private Arguments() {
        // Prevent creation of Arguments objects.
    }

    public static Configuration parse(String[] args) {
        validateInputArgument(args);

        Optional<String> outputFilename =
                getOutputFilename(args);

        int representativeCount =
                getRepresentativeCount(args);

        ApportionmentMethod method =
                getApportionmentMethod(args);

        return new Configuration(
                args[0],
                representativeCount,
                method,
                outputFilename
        );
    }

    private static void validateInputArgument(String[] args) {
        if (args == null
                || args.length == 0
                || args[0] == null
                || args[0].isBlank()
                || args[0].startsWith("--")) {

            throw new IllegalArgumentException(
                    "The population CSV file must be "
                            + "the first argument."
            );
        }
    }

    private static int getRepresentativeCount(String[] args) {
        if (args.length < 2) {
            return DEFAULT_REPRESENTATIVE_COUNT;
        }

        try {
            int representativeCount =
                    Integer.parseInt(args[1]);

            if (representativeCount <= 0) {
                throw new IllegalArgumentException(
                        "The number of representatives "
                                + "must be greater than zero."
                );
            }

            return representativeCount;
        } catch (NumberFormatException exception) {
            return DEFAULT_REPRESENTATIVE_COUNT;
        }
    }

    private static ApportionmentMethod getApportionmentMethod(
            String[] args
    ) {
        for (String argument : args) {
            if ("--hamilton".equals(argument)) {
                return new HamiltonApportionment();
            }
        }

        return new JeffersonApportionment();
    }

    private static Optional<String> getOutputFilename(
            String[] args
    ) {
        Optional<String> outputFilename = Optional.empty();

        for (int index = 1; index < args.length; index++) {
            if ("--out".equals(args[index])) {
                if (index + 1 >= args.length) {
                    throw invalidOutputFilenameException();
                }

                String filename = args[index + 1];

                if (!OUTPUT_FILENAME_PATTERN
                        .matcher(filename)
                        .matches()) {
                    throw invalidOutputFilenameException();
                }

                outputFilename = Optional.of(filename);
            }
        }

        return outputFilename;
    }

    private static IllegalArgumentException
    invalidOutputFilenameException() {
        return new IllegalArgumentException(
                "A valid output filename must immediately "
                        + "follow --out. The filename may contain "
                        + "only letters, numbers, and underscores, "
                        + "and must end in .csv."
        );
    }
}
