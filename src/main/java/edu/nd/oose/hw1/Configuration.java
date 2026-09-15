package edu.nd.oose.hw1;

import java.util.Objects;
import java.util.Optional;

public final class Configuration {

    private final String inputFilename;
    private final int representativeCount;
    private final ApportionmentMethod apportionmentMethod;
    private final Optional<String> outputFilename;

    public Configuration(
            String inputFilename,
            int representativeCount,
            ApportionmentMethod apportionmentMethod,
            Optional<String> outputFilename
    ) {
        this.inputFilename = Objects.requireNonNull(
                inputFilename
        );
        this.representativeCount = representativeCount;
        this.apportionmentMethod = Objects.requireNonNull(
                apportionmentMethod
        );
        this.outputFilename = Objects.requireNonNull(
                outputFilename
        );
    }

    public String getInputFilename() {
        return inputFilename;
    }

    public int getRepresentativeCount() {
        return representativeCount;
    }

    public ApportionmentMethod getApportionmentMethod() {
        return apportionmentMethod;
    }

    public Optional<String> getOutputFilename() {
        return outputFilename;
    }
}
