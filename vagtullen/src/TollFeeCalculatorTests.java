import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class TollFeeCalculatorTests {
    private TollFeeCalculator calculator = new TollFeeCalculator("Lab4.txt");

    @Test
    void getTotalFeeCost() {
        var dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        int result = calculator.getTotalFeeCost(new LocalDateTime[] {
            LocalDateTime.parse("2020-06-30 00:05", dateTimeFormatter),
            LocalDateTime.parse("2020-06-30 06:34", dateTimeFormatter),
            LocalDateTime.parse("2020-06-30 08:52", dateTimeFormatter),
            LocalDateTime.parse("2020-06-30 10:13", dateTimeFormatter),
            LocalDateTime.parse("2020-06-30 10:25", dateTimeFormatter),
            LocalDateTime.parse("2020-06-30 11:04", dateTimeFormatter),
            LocalDateTime.parse("2020-06-30 16:50", dateTimeFormatter),
            LocalDateTime.parse("2020-06-30 18:00", dateTimeFormatter),
            LocalDateTime.parse("2020-06-30 21:30", dateTimeFormatter),
            LocalDateTime.parse("2020-07-01 00:00", dateTimeFormatter)
        });

        assertEquals(55, result);
    }

    @Test
    void getTollFeePerPassing() {
        var dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        int result1 = calculator.getTollFeePerPassing(LocalDateTime.parse("2020-06-30 00:05", dateTimeFormatter));
        assertEquals(0, result1);

        int result2 = calculator.getTollFeePerPassing(LocalDateTime.parse("2020-06-30 06:34", dateTimeFormatter));
        assertEquals(13, result2);

        int result3 = calculator.getTollFeePerPassing(LocalDateTime.parse("2020-06-30 08:52", dateTimeFormatter));
        assertEquals(8, result3);

        int result4 = calculator.getTollFeePerPassing(LocalDateTime.parse("2020-06-30 10:13", dateTimeFormatter));
        assertEquals(8, result4);

        int result5 = calculator.getTollFeePerPassing(LocalDateTime.parse("2020-06-30 10:25", dateTimeFormatter));
        assertEquals(8, result5);

        int result6 = calculator.getTollFeePerPassing(LocalDateTime.parse("2020-06-30 11:04", dateTimeFormatter));
        assertEquals(8, result6);

        int result7 = calculator.getTollFeePerPassing(LocalDateTime.parse("2020-06-30 16:50", dateTimeFormatter));
        assertEquals(18, result7);

        int result8 = calculator.getTollFeePerPassing(LocalDateTime.parse("2020-06-30 18:00", dateTimeFormatter));
        assertEquals(8, result8);

        int result9 = calculator.getTollFeePerPassing(LocalDateTime.parse("2020-06-30 21:30", dateTimeFormatter));
        assertEquals(0, result9);

        int result10 = calculator.getTollFeePerPassing(LocalDateTime.parse("2020-07-01 00:00", dateTimeFormatter));
        assertEquals(0, result10);
    }

    @Test
    void isTollFreeDate() {
        var dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        boolean result1 = calculator.isTollFreeDate(LocalDateTime.parse("2020-06-30 00:05", dateTimeFormatter));
        boolean result2 = calculator.isTollFreeDate(LocalDateTime.parse("2020-06-30 06:34", dateTimeFormatter));
        boolean result3 = calculator.isTollFreeDate(LocalDateTime.parse("2020-06-30 08:52", dateTimeFormatter));
        boolean result4 = calculator.isTollFreeDate(LocalDateTime.parse("2020-06-30 10:13", dateTimeFormatter));
        boolean result5 = calculator.isTollFreeDate(LocalDateTime.parse("2020-06-30 10:25", dateTimeFormatter));
        boolean result6 = calculator.isTollFreeDate(LocalDateTime.parse("2020-06-30 11:04", dateTimeFormatter));
        boolean result7 = calculator.isTollFreeDate(LocalDateTime.parse("2020-06-30 16:50", dateTimeFormatter));
        boolean result8 = calculator.isTollFreeDate(LocalDateTime.parse("2020-06-30 18:00", dateTimeFormatter));
        boolean result9 = calculator.isTollFreeDate(LocalDateTime.parse("2020-06-30 21:30", dateTimeFormatter));
        boolean result10 = calculator.isTollFreeDate(LocalDateTime.parse("2020-07-01 00:00", dateTimeFormatter));

        assertFalse(result1);
        assertTrue(result2);
        assertTrue(result3);
        assertTrue(result4);
        assertTrue(result5);
        assertTrue(result6);
        assertTrue(result7);
        assertTrue(result8);
        assertFalse(result9);
        assertFalse(result10);
    }
}
