package com.rkukharenka.telegrambot.instaboxbot.common.utils;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateTimeUtils {

    public static final String DATE_FORMAT = "dd-MM-yyyy";
    public static final String TIME_FORMAT = "HH:mm";

    public static String formatDateToString(@NotNull LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public static LocalDate formatStringToLocalDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public static boolean isDate(String text) {
        try {
            DateTimeFormatter.ofPattern(DATE_FORMAT).parse(text);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    public static LocalTime formatStringToLocalTime(String time) {
        return LocalTime.parse(time, DateTimeFormatter.ofPattern(TIME_FORMAT));
    }

    public static String formatLocalTimeToString(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern(TIME_FORMAT));
    }

    public static String freeTimeSlotsToString(List<LocalDateTime[]> timeSlots) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_FORMAT);

        return IntStream.range(0, timeSlots.size())
                .mapToObj(i -> formatTimeSlot(i + 1, timeSlots.get(i), formatter))
                .collect(Collectors.joining(",\n"));
    }

    private static String formatTimeSlot(int index, LocalDateTime[] timeSlot, DateTimeFormatter formatter) {
        String startTime = formatter.format(timeSlot[0]);
        String endTime = formatter.format(timeSlot[1]);
        return "%d. %s-%s".formatted(index, startTime, endTime);
    }

}
