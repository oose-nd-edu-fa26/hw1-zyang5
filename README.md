# HW1A and HW1B - Responding to Change

Name: Ray Yang
NetID: zyang5

## Description

This program reads state population data from a CSV file. It calculates how many representatives each state receives and prints the states in alphabetical order.

The program uses:

* 435 representatives by default
* Jefferson’s method by default
* Hamilton’s method when `--hamilton` is included

The program finds the `State` and `Population` columns by reading the CSV header. The columns can appear in any order, and capitalization and extra spaces are ignored.

## Build

```bash
./gradlew clean build
```

The JAR file will be created here:

```text
build/libs/Apportionment.jar
```

## Run

Use Jefferson with 435 representatives:

```bash
java -jar build/libs/Apportionment.jar part2_input.csv
```

Use Jefferson with 100 representatives:

```bash
java -jar build/libs/Apportionment.jar part2_input.csv 100
```

Use Hamilton with 100 representatives:

```bash
java -jar build/libs/Apportionment.jar part2_input.csv 100 --hamilton
```

Write the results to a CSV file:

```bash
java -jar build/libs/Apportionment.jar part2_input.csv --out results.csv
```

The representative count must appear immediately after the input filename.

An output filename may contain only letters, numbers, and underscores. It must end in `.csv`.

## Tests

Run all tests with:

```bash
./gradlew clean test
```

## Contributions

### Part 1 - Zihui Yang

* Set up the Gradle project and executable JAR.
* Created the `State` class.
* Read state and population data from a CSV file.
* Handled invalid CSV rows and invalid arguments.
* Implemented Hamilton’s method.
* Printed results in alphabetical order.
* Added JUnit tests.

### Part 2 - Zihui Yang

* Added support for CSV columns in different positions.
* Made CSV headings case-insensitive.
* Implemented Jefferson’s method and made it the default.
* Added the `--hamilton` option.
* Added the `--out` option.
* Added CSV file output.
* Added interfaces for apportionment methods and output formats.
* Added tests for the new features and error cases.

## Issues

No known issues.
