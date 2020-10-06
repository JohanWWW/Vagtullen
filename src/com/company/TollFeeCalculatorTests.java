package com.company;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class TollFeeCalculatorTests {

    @Test
    @DisplayName("Validate that the last LocalDateTime that is printed to the console is correct")
    void tollFeeCalculatorPrintsLastLocalDateTime() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        // Act
        new TollFeeCalculator("testdata/test_dates.txt");

        String[] lines = outputStream.toString()
                                     .replace("\r\n", "\n") // Windows
                                     .replace("\r", "\n") // MacOS
                                     .split("\n");
        String actualLastDate = lines[lines.length - 2]; // <- This is the date that is right above the result line

        printStream.close();

        // Assert
        String expectedLastDate = "2020-07-14T03:05";

        assertEquals(expectedLastDate, actualLastDate, "The last LocalDateTime that is printed to the console is not correct");
    }

    @Test
    @DisplayName("Validate that getTotalFeeCost prints correct output to the console")
    void tollFeeCalculatorPrintsCorrectOutput() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        // Act
        new TollFeeCalculator("testdata/test_dates.txt"); // Discard result because we are only interested of what is printed to the console

        String actualConsoleOutput = outputStream.toString()
                                                 .replace("\r\n", "\n") // Windows
                                                 .replace("\r", "\n"); // MacOS

        printStream.close();

        // Assert
        String expectedConsoleOutput =
            "2020-06-06T18:19\n" +
            "2020-06-07T16:42\n" +
            "2020-06-11T16:11\n" +
            "2020-06-14T07:46\n" +
            "2020-06-15T14:47\n" +
            "2020-06-18T19:18\n" +
            "2020-06-18T19:34\n" +
            "2020-06-18T23:41\n" +
            "2020-06-20T12:25\n" +
            "2020-06-20T14:23\n" +
            "2020-06-22T19:23\n" +
            "2020-06-27T04:42\n" +
            "2020-06-28T02:57\n" +
            "2020-06-28T17:25\n" +
            "2020-06-28T19:44\n" +
            "2020-06-29T16:08\n" +
            "2020-07-01T13:47\n" +
            "2020-07-12T04:11\n" +
            "2020-07-14T02:14\n" +
            "2020-07-14T03:05\n" +
            "The total fee for the inputfile is44\n"; // <- Missing whitespace :(

        assertEquals(expectedConsoleOutput, actualConsoleOutput, "The console output is incorrect");
    }

    @Test
    @DisplayName("Validate that the fee cost never exceed 60 SEK per day")
    void getTotalFeeCostReturnsNoMoreThanMaxPerDay() {
        // Arrange
        LocalDateTime[] testDates = {
            createDateTime("2020-06-09 06:50"),
            createDateTime("2020-06-09 07:40"),
            createDateTime("2020-06-09 10:43"),
            createDateTime("2020-06-09 11:02"),
            createDateTime("2020-06-09 11:26"),
            createDateTime("2020-06-09 13:29"),
            createDateTime("2020-06-09 16:16"),
            createDateTime("2020-06-09 17:43"),
            createDateTime("2020-06-09 17:59"),
            createDateTime("2020-06-09 18:11"),
        };
        // Total: 65 SEK before discount

        // Act
        int result = TollFeeCalculator.getTotalFeeCost(testDates);

        // Assert
        assertEquals(60, result, "The fee cost exceeded 60 SEK");
    }

    @Test
    @DisplayName("Validate that the total fee cost is calculated correctly")
    void getTotalFeeCost() {
        // Arrange & Act
        int result = TollFeeCalculator.getTotalFeeCost(new LocalDateTime[] {
            createDateTime("2020-06-30 00:05"),
            createDateTime("2020-06-30 06:34"),
            createDateTime("2020-06-30 08:52"),
            createDateTime("2020-06-30 10:13"),
            createDateTime("2020-06-30 10:25"),
            createDateTime("2020-06-30 11:04"),
            createDateTime("2020-06-30 16:50"),
            createDateTime("2020-06-30 18:00"),
            createDateTime("2020-06-30 21:30"),
            createDateTime("2020-07-01 00:00")
        });

        // Assert
        assertEquals(55, result, "The total fee cost is incorrect");
    }

    @Test
    @DisplayName("Validate that getTotalFeeCost prints localDateTime in chronological order to the console")
    void getTotalFeeCostPrintsCorrectDateOrder() {
        /*
            We need to listen to the output stream because there is no way to
            test a variable inside the method that is being tested
        */

        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        LocalDateTime[] unorderedDates = {
            createDateTime("2020-10-05 18:00"),
            createDateTime("2020-10-05 15:30"),
            createDateTime("2020-10-05 17:00"),
            createDateTime("2020-10-05 06:30")
        };

        // Act
        TollFeeCalculator.getTotalFeeCost(unorderedDates);

        String actualConsoleOutput = outputStream.toString()
                                                 .replace("\r\n", "\n") // Windows
                                                 .replace("\r", "\n"); // MacOS

        printStream.close();

        // Assert
        String expectedConsoleOutput =
            "2020-10-05T06:30\n" +
            "2020-10-05T15:30\n" +
            "2020-10-05T17:00\n" +
            "2020-10-05T18:00\n";

        assertEquals(expectedConsoleOutput, actualConsoleOutput, "Values from the input file where not sorted correctly by the program");
    }

    @Test
    @DisplayName("Validate that the total fee includes the subtotal for each day")
    void getTotalFeeCostForOverlappingDays() {
        // Arrange
        LocalDateTime[] testDates = {
            // First day
            createDateTime("2020-10-05 06:30"),
            createDateTime("2020-10-05 07:59"),
            createDateTime("2020-10-05 15:00"),
            createDateTime("2020-10-05 15:30"),
            createDateTime("2020-10-05 16:30"),
            createDateTime("2020-10-05 17:00"),
            createDateTime("2020-10-05 18:00"),

            // Second day
            createDateTime("2020-10-06 06:30"),
            createDateTime("2020-10-06 07:59"),
            createDateTime("2020-10-06 15:00"),
            createDateTime("2020-10-06 15:30"),
            createDateTime("2020-10-06 16:30"),
            createDateTime("2020-10-06 17:00"),
            createDateTime("2020-10-06 18:00")
        };

        // Act
        int result = TollFeeCalculator.getTotalFeeCost(testDates);

        // Assert
        assertEquals(120, result, "The grand total is not correct"); // 60 + 60 = 120
    }

    @Test
    @DisplayName("Validate that toll fee per passing is correct")
    void getTollFeePerPassing() {
        // Arrange & Act
        int result1 = TollFeeCalculator.getTollFeePerPassing(createDateTime("2020-06-30 00:05"));
        int result2 = TollFeeCalculator.getTollFeePerPassing(createDateTime("2020-06-30 06:34"));
        int result3 = TollFeeCalculator.getTollFeePerPassing(createDateTime("2020-06-30 08:52"));
        int result4 = TollFeeCalculator.getTollFeePerPassing(createDateTime("2020-06-30 10:13"));
        int result5 = TollFeeCalculator.getTollFeePerPassing(createDateTime("2020-06-30 10:25"));
        int result6 = TollFeeCalculator.getTollFeePerPassing(createDateTime("2020-06-30 11:04"));
        int result7 = TollFeeCalculator.getTollFeePerPassing(createDateTime("2020-06-30 16:50"));
        int result8 = TollFeeCalculator.getTollFeePerPassing(createDateTime("2020-06-30 18:00"));
        int result9 = TollFeeCalculator.getTollFeePerPassing(createDateTime("2020-06-30 21:30"));
        int result10 = TollFeeCalculator.getTollFeePerPassing(createDateTime("2020-07-01 00:00"));

        // Assert
        String assertFailedMessage = "The toll fee per passing is incorrect";

        assertEquals(0, result1, assertFailedMessage);
        assertEquals(13, result2, assertFailedMessage);
        assertEquals(8, result3, assertFailedMessage);
        assertEquals(8, result4, assertFailedMessage);
        assertEquals(8, result5, assertFailedMessage);
        assertEquals(8, result6, assertFailedMessage);
        assertEquals(18, result7, assertFailedMessage);
        assertEquals(8, result8, assertFailedMessage);
        assertEquals(0, result9, assertFailedMessage);
        assertEquals(0, result10, assertFailedMessage);
    }

    @Test
    @DisplayName("Validate that NoSuchElementException is handled when the file is empty")
    void tollFeeCalculatorCatchesNoSuchElementException() {
        // Arrange
        String pathToEmptyFile = "testdata/empty_file.txt";

        try {
            // Act
            new TollFeeCalculator(pathToEmptyFile);
        } catch (NoSuchElementException e) {
            fail("NoSuchElementException was unhandled", e);
        }

        // Assert
    }

    @Test
    @DisplayName("Validate that DateTimeParseException is handled when parsing a string that is not yyyy-MM-dd HH:mm formatted")
    void tollFeeCalculatorCatchesDateTimeParseException() {
        // Arrange
        String testFilePath = "testdata/incorrect_datetime_format.txt";

        try {
            // Act
            new TollFeeCalculator(testFilePath);
        } catch (DateTimeParseException e) {
            fail("DateTimeParseException was unhandled", e);
        }

        // Assert
    }

    @Test
    @DisplayName("Validate that NullPointerException is handled when null passed as filename to the constructor")
    void tollFeeCalculatorCatchesNullPointerException() {
        try {
            // Arrange & Act
            new TollFeeCalculator(null);
        } catch (NullPointerException e) {
            fail("NullPointerException was unhandled", e);
        }

        // Assert
    }

    @Test
    @DisplayName("Validate that ArrayOutOfBoundsException is not thrown when the input file contains only one entry")
    void tollFeeCalculatorDoesNotThrowArrayOutOfBoundsException() {
        // Arrange
        String testFilePath = "testdata/single_datetime.txt";

        try {
            // Act
            new TollFeeCalculator(testFilePath);
        } catch (ArrayIndexOutOfBoundsException e) {
            fail("ArrayIndexOutOfBoundsException was unhandled", e);
        }

        // Assert
    }

    private static LocalDateTime createDateTime(CharSequence dateTimeString) {
        return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    // Create date component and add time component 00:00 (returns LocalDateTime)
    private static LocalDateTime createDate(CharSequence dateString) {
        LocalDate localDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return LocalDateTime.of(localDate, LocalTime.MIN);
    }
}
