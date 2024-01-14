package com.rkukharenka.telegrambot.instaboxbot.common.service.impl;

import com.rkukharenka.telegrambot.instaboxbot.common.entity.Order;
import com.rkukharenka.telegrambot.instaboxbot.common.repository.OrderRepository;
import com.rkukharenka.telegrambot.instaboxbot.common.service.DateTimeBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DateTimeBookingServiceImpl implements DateTimeBookingService {

    private final OrderRepository orderRepository;

    @Value("${user-preferences.working-hours.start}")
    private int[] startWorkingDay;
    @Value("${user-preferences.working-hours.end}")
    private int[] endWorkingDay;

    @Override
    public List<LocalDateTime[]> getFreeTimeSlotsBySpecificDate(LocalDate specificDate) {
        List<Order> orders = orderRepository.findOrdersBySpecificDate(specificDate);

        //working hours from 8AM to 11PM
        LocalDateTime currentDateTime = specificDate.atTime(startWorkingDay[0], startWorkingDay[1]);
        LocalDateTime endOfDay = specificDate.atTime(endWorkingDay[0], endWorkingDay[1]);

        List<LocalDateTime[]> freeTimePeriods = new ArrayList<>();

        for (Order order : orders) {
            if (currentDateTime.isBefore(order.getStartOrder())) {
                LocalDateTime endFreeTime = order.getStartOrder().minusHours(1);
                long duration = Duration.between(currentDateTime, endFreeTime).toHours();
                if (!endFreeTime.isBefore(currentDateTime) && duration >= 1) {
                    freeTimePeriods.add(new LocalDateTime[]{currentDateTime, endFreeTime});
                }
            }
            currentDateTime = order.getFinishOrder().plusHours(1);
        }

        long duration = Duration.between(currentDateTime, endOfDay).toHours();
        if (currentDateTime.isBefore(endOfDay) && duration >= 1) {
            freeTimePeriods.add(new LocalDateTime[]{currentDateTime, endOfDay});
        }

        return freeTimePeriods;
    }

    @Override
    public boolean isValidTimeForBooking(LocalDateTime startTime, LocalDateTime endTime) {
        LocalDate bookingDate = startTime.toLocalDate();
        List<LocalDateTime[]> freeTimeSlots = getFreeTimeSlotsBySpecificDate(bookingDate);

        return freeTimeSlots.stream()
                .anyMatch(freeSlot ->
                        (startTime.isEqual(freeSlot[0]) || startTime.isAfter(freeSlot[0]))
                        && (endTime.isEqual(freeSlot[1]) || endTime.isBefore(freeSlot[1]))
                );
    }

    @Override
    public Set<LocalDate> getFreeDaysForMonth(YearMonth yearMonth) {// TODO: 07-Jan-24  
        List<Order> ordersByMonth = orderRepository.findOrdersByMonth(yearMonth.getMonthValue(), yearMonth.getYear());

        Map<LocalDate, List<Order>> ordersByDate = ordersByMonth.stream()
                .collect(Collectors.groupingBy(order -> order.getStartOrder().toLocalDate()));

        Set<LocalDate> freeDaysInMonth = new HashSet<>();

        int daysInMonth = yearMonth.lengthOfMonth();
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = yearMonth.atDay(day);

            if (ordersByDate.containsKey(date)) {
                //working hours from 8AM to 11PM
                LocalDateTime currentDateTime = date.atTime(startWorkingDay[0], startWorkingDay[1]);
                LocalDateTime endOfDay = date.atTime(endWorkingDay[0], endWorkingDay[1]);

                for (Order order : ordersByDate.get(date)) {
                    if (currentDateTime.isBefore(order.getStartOrder())) {
                        LocalDateTime endFreeTime = order.getStartOrder().minusHours(1);
                        long duration = Duration.between(currentDateTime, endFreeTime).toHours();
                        if (!endFreeTime.isBefore(currentDateTime) && duration >= 1) {
                            freeDaysInMonth.add(date);
                            break;
                        }
                    }
                    currentDateTime = order.getFinishOrder().plusHours(1);
                }

                long duration = Duration.between(currentDateTime, endOfDay).toHours();
                if (currentDateTime.isBefore(endOfDay) && duration >= 1) {
                    freeDaysInMonth.add(date);
                }

            } else {
                freeDaysInMonth.add(date);
            }

        }

        return freeDaysInMonth;
    }

}
