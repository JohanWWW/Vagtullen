import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class TollFeeCalculatorTests {

    @Test
    @DisplayName("Validate that toll fees are calculated correctly")
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
        assertEquals(55, result);
    }

    @Test
    @DisplayName("Validate that getTotalFeeCost prints localDateTime in chronological order to the console")
    void getTotalFeeCostPrintsCorrectDateOrder() {
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

        assertEquals(expectedConsoleOutput, actualConsoleOutput);
    }

    @Test
    @DisplayName("Validate that toll fees are calculated for each day")
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
        assertEquals(120, result); // 60 + 60 = 120
    }

    @Test
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
        assertEquals(0, result1);
        assertEquals(13, result2);
        assertEquals(8, result3);
        assertEquals(8, result4);
        assertEquals(8, result5);
        assertEquals(8, result6);
        assertEquals(18, result7);
        assertEquals(8, result8);
        assertEquals(0, result9);
        assertEquals(0, result10);
    }

    @Test
    @DisplayName("Validate isTollFreeDate returns correct values")
    void isTollFreeDate() {
        /*
            Toll free dates are:
                * Saturday
                * Sunday
                * Any day in the month of July
         */

        // Arrange
        LocalDateTime tuesdayInJune = createDate("2020-06-16");
        LocalDateTime saturdayInJune = createDate("2020-06-27");
        LocalDateTime sundayInJuly = createDate("2020-07-19");
        LocalDateTime thursdayInJuly = createDate("2020-07-30");
        LocalDateTime fridayInAugust = createDate("2020-08-07");

        // Act
        boolean result1 = TollFeeCalculator.isTollFreeDate(tuesdayInJune);
        boolean result2 = TollFeeCalculator.isTollFreeDate(saturdayInJune);
        boolean result3 = TollFeeCalculator.isTollFreeDate(sundayInJuly);
        boolean result4 = TollFeeCalculator.isTollFreeDate(thursdayInJuly);
        boolean result5 = TollFeeCalculator.isTollFreeDate(fridayInAugust);

        // Assert
        assertFalse(result1);
        assertTrue(result2);
        assertTrue(result3);
        assertTrue(result4);
        assertFalse(result5);
    }

    private static LocalDateTime[] deserializeFile(String filePath) throws FileNotFoundException {
        var file = new File(filePath);

        LocalDateTime[] localDateTimeArray;

        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
            var localDateTimeList = new ArrayList<LocalDateTime>();

            while (scanner.hasNextLine()) {
                LocalDateTime localDateTime = createDateTime(scanner.nextLine());
                localDateTimeList.add(localDateTime);
            }

            localDateTimeArray = new LocalDateTime[localDateTimeList.size()];
            localDateTimeList.toArray(localDateTimeArray);

            return localDateTimeArray;

        } catch (FileNotFoundException e) {
            throw e;
        } finally {
            // Creating an instance of Scanner can fail and remain null
            if (scanner != null)
                scanner.close();
        }
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
