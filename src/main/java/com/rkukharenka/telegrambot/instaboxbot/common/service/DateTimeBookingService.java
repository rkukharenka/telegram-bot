package com.rkukharenka.telegrambot.instaboxbot.common.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Set;

public interface DateTimeBookingService {

    List<LocalDateTime[]> getFreeTimeSlotsBySpecificDate(LocalDate specificDate);

    boolean isValidTimeForBooking(LocalDateTime startTime, LocalDateTime endTime);

    Set<LocalDate> getFreeDaysForMonth(YearMonth yearMonth);

}
