import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

public class TollFeeCalculatorTests {

    @Test
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
    void isTollFreeDate() {
        // Arrange & Act
        boolean result1 = TollFeeCalculator.isTollFreeDate(createDate("2020-06-30"));
        boolean result2 = TollFeeCalculator.isTollFreeDate(createDate("2020-06-30"));
        boolean result3 = TollFeeCalculator.isTollFreeDate(createDate("2020-06-30"));
        boolean result4 = TollFeeCalculator.isTollFreeDate(createDate("2020-06-30"));
        boolean result5 = TollFeeCalculator.isTollFreeDate(createDate("2020-06-30"));
        boolean result6 = TollFeeCalculator.isTollFreeDate(createDate("2020-06-30"));
        boolean result7 = TollFeeCalculator.isTollFreeDate(createDate("2020-06-30"));
        boolean result8 = TollFeeCalculator.isTollFreeDate(createDate("2020-06-30"));
        boolean result9 = TollFeeCalculator.isTollFreeDate(createDate("2020-06-30"));
        boolean result10 = TollFeeCalculator.isTollFreeDate(createDate("2020-07-01"));

        // Assert
        assertFalse(result1);
        assertFalse(result2);
        assertFalse(result3);
        assertFalse(result4);
        assertFalse(result5);
        assertFalse(result6);
        assertFalse(result7);
        assertFalse(result8);
        assertFalse(result9);
        assertTrue(result10); // The month of July is toll free
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
