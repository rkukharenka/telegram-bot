package com.rkukharenka.telegrambot.instaboxbot.common.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

class DateTimeUtilsTest {

    @Test
    void testFreeTimeSlotsToString() {
        List<LocalDateTime[]> freeTimeSlots = List.of(new LocalDateTime[]{LocalDateTime.of(2023, 12, 1, 8, 0), LocalDateTime.of(2023, 12, 1, 9, 0)},
                new LocalDateTime[]{LocalDateTime.of(2023, 12, 1, 17, 0), LocalDateTime.of(2023, 12, 1, 18, 0)},
                new LocalDateTime[]{LocalDateTime.of(2023, 12, 1, 22, 0), LocalDateTime.of(2023, 12, 1, 23, 0)});
        String expected = "1. 08:00-09:00,\n2. 17:00-18:00,\n3. 22:00-23:00";

        String actual = DateTimeUtils.freeTimeSlotsToString(freeTimeSlots);

        Assertions.assertEquals(expected, actual);
    }
}