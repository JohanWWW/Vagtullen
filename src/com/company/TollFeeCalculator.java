package com.company;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class TollFeeCalculator {

    public TollFeeCalculator(String inputFile) {
        Scanner sc = null;
        try {
            sc = new Scanner(new File(inputFile));
            String[] dateStrings = sc.nextLine().split(", ");
            LocalDateTime[] dates = new LocalDateTime[dateStrings.length];
            for(int i = 0; i < dates.length; i++) {
                dates[i] = LocalDateTime.parse(dateStrings[i], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            }
            System.out.println("The total fee for the inputfile is" + getTotalFeeCost(dates));
        } catch (IOException e) {
            System.err.println("Could not read file " + inputFile);
        } catch (NullPointerException e) {
            System.err.println("Path is null");
        } catch (DateTimeParseException e) {
            System.err.println(e.getParsedString() + " is not a valid LocalDateTime-string");
        } catch (NoSuchElementException e) {
            System.err.println("Could not find line");
        } finally {
            if (sc != null)
                sc.close();
        }
    }

    public static int getTotalFeeCost(LocalDateTime[] dates) {
        int grandTotalFee = 0;

        // This groups LocalDateTime instances that have the same LocalDate component, into arrays resulting in a collection of arrays
        Map<LocalDate, List<LocalDateTime>> datesByDateTime = Arrays.stream(dates).collect(Collectors.groupingBy(LocalDateTime::toLocalDate, Collectors.toList()));
        LocalDate[] localDates = datesByDateTime.keySet().stream().sorted(Comparator.naturalOrder()).toArray(LocalDate[]::new); // Map keys to array

        // Iterate through each day
        for (LocalDate currentDate: localDates) {
            var currentDayCollection = datesByDateTime.get(currentDate).stream().sorted(Comparator.naturalOrder()).toArray(LocalDateTime[]::new);
            grandTotalFee += calculateFeeCost(currentDayCollection);
        }

        return grandTotalFee;
    }

    public static int getTollFeePerPassing(LocalDateTime date) {
        if (isTollFreeDate(date)) return 0;
        int hour = date.getHour();
        int minute = date.getMinute();
        if (hour == 6 && minute >= 0 && minute <= 29) return 8;
        else if (hour == 6 && minute >= 30 && minute <= 59) return 13;
        else if (hour == 7 && minute >= 0 && minute <= 59) return 18;
        else if (hour == 8 && minute >= 0 && minute <= 29) return 13;
        else if (hour >= 8 && hour <= 14 && minute <= 59) return 8;
        else if (hour == 15 && minute >= 0 && minute <= 29) return 13;
        else if (hour == 15 && minute >= 0 || hour == 16 && minute <= 59) return 18;
        else if (hour == 17 && minute >= 0 && minute <= 59) return 13;
        else if (hour == 18 && minute >= 0 && minute <= 29) return 8;
        else return 0;
    }

    public static boolean isTollFreeDate(LocalDateTime date) {
        return date.getDayOfWeek().getValue() == 6 || date.getDayOfWeek().getValue() == 7 || date.getMonth().getValue() == 7;
    }

    public static void main(String[] args) {
        new TollFeeCalculator("Lab4.txt");
    }

    private static int calculateFeeCost(LocalDateTime[] batchedDay) {
        int totalFee = 0;
        LocalDateTime intervalStartDateTime = batchedDay[0];
        for(LocalDateTime date: batchedDay) {
            System.out.println(date.toString());
            long diffInMinutes = intervalStartDateTime.until(date, ChronoUnit.MINUTES);
            if (diffInMinutes > 60 || date.equals(intervalStartDateTime)) {
                totalFee += getTollFeePerPassing(date);
                intervalStartDateTime = date;
            } else {
                int currentFee = getTollFeePerPassing(date);
                int previousFee = getTollFeePerPassing(intervalStartDateTime);
                if (currentFee > previousFee) {
                    totalFee += currentFee;
                }
            }
        }
        return Math.min(totalFee, 60);
    }
}
