package com.rkukharenka.telegrambot.instaboxbot.service.impl;

import com.rkukharenka.telegrambot.instaboxbot.common.entity.Order;
import com.rkukharenka.telegrambot.instaboxbot.common.repository.OrderRepository;
import com.rkukharenka.telegrambot.instaboxbot.common.service.impl.DateTimeBookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DateTimeBookingServiceImplTest {

    private List<Order> ordersBySpecificDate;
    private List<Order> ordersByMonth;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private DateTimeBookingServiceImpl dateTimeBookingService;

    @BeforeEach
    void setUp() {
        ordersBySpecificDate = List.of(
                new Order().setStartOrder(LocalDateTime.of(2023, 12, 1, 8, 30)).setFinishOrder(LocalDateTime.of(2023, 12, 1, 10, 30)),
                new Order().setStartOrder(LocalDateTime.of(2023, 12, 1, 13, 0)).setFinishOrder(LocalDateTime.of(2023, 12, 1, 16, 0)),
                new Order().setStartOrder(LocalDateTime.of(2023, 12, 1, 19, 0)).setFinishOrder(LocalDateTime.of(2023, 12, 1, 21, 0))
        );
        ordersByMonth = List.of(
                new Order().setStartOrder(LocalDateTime.of(2023, 12, 1, 10, 0)).setFinishOrder(LocalDateTime.of(2023, 12, 1, 12, 0)),
                new Order().setStartOrder(LocalDateTime.of(2023, 12, 1, 13, 0)).setFinishOrder(LocalDateTime.of(2023, 12, 1, 16, 0)),
                new Order().setStartOrder(LocalDateTime.of(2023, 12, 1, 19, 0)).setFinishOrder(LocalDateTime.of(2023, 12, 1, 21, 0)),
                new Order().setStartOrder(LocalDateTime.of(2023, 12, 5, 8, 0)).setFinishOrder(LocalDateTime.of(2023, 12, 5, 23, 0)),
                new Order().setStartOrder(LocalDateTime.of(2023, 12, 10, 8, 0)).setFinishOrder(LocalDateTime.of(2023, 12, 10, 23, 0)),
                new Order().setStartOrder(LocalDateTime.of(2023, 12, 15, 8, 0)).setFinishOrder(LocalDateTime.of(2023, 12, 15, 23, 0)),
                new Order().setStartOrder(LocalDateTime.of(2023, 12, 20, 8, 0)).setFinishOrder(LocalDateTime.of(2023, 12, 20, 23, 0)),
                new Order().setStartOrder(LocalDateTime.of(2023, 12, 25, 8, 0)).setFinishOrder(LocalDateTime.of(2023, 12, 25, 23, 0)),
                new Order().setStartOrder(LocalDateTime.of(2023, 12, 30, 8, 0)).setFinishOrder(LocalDateTime.of(2023, 12, 30, 23, 0))
        );
    }

    @Test
    void testFindFreeTimeSlots_ifHaveRecordsOnSpecificDay() {
        //given
        List<LocalDateTime[]> expectedResult = List.of(
                new LocalDateTime[]{LocalDateTime.of(2023, 12, 1, 17, 0), LocalDateTime.of(2023, 12, 1, 18, 0)},
                new LocalDateTime[]{LocalDateTime.of(2023, 12, 1, 22, 0), LocalDateTime.of(2023, 12, 1, 23, 0)}
        );
        when(orderRepository.findOrdersBySpecificDate(any())).thenReturn(ordersBySpecificDate);

        //when
        List<LocalDateTime[]> actualResult = dateTimeBookingService.getFreeTimeSlotsBySpecificDate(LocalDate.of(2023, 12, 1));

        //then
        assertThat(actualResult).hasSameElementsAs(expectedResult);
    }

    @Test
    void testFindFreeTimeSlots_ifAllDayFree() {
        //given
        List<LocalDateTime[]> expectedResult = Collections.singletonList(
                new LocalDateTime[]{LocalDateTime.of(2023, 12, 1, 8, 0), LocalDateTime.of(2023, 12, 1, 23, 0)}
        );
        when(orderRepository.findOrdersBySpecificDate(any())).thenReturn(List.of());

        //when
        List<LocalDateTime[]> actualResult = dateTimeBookingService.getFreeTimeSlotsBySpecificDate(LocalDate.of(2023, 12, 1));

        //then
        assertThat(actualResult).hasSameElementsAs(expectedResult);
    }

    @Test
    void testTimeIsValidTimeForBooking() {
        //given
        when(orderRepository.findOrdersBySpecificDate(any())).thenReturn(ordersBySpecificDate);

        assertAll("is valid",
                () -> assertTrue(dateTimeBookingService.isValidTimeForBooking(LocalDateTime.of(2023, 12, 1, 17, 0), LocalDateTime.of(2023, 12, 1, 18, 0)), "17:00-18:00"),
                () -> assertTrue(dateTimeBookingService.isValidTimeForBooking(LocalDateTime.of(2023, 12, 1, 22, 0), LocalDateTime.of(2023, 12, 1, 23, 0)), "22:00-23:00")
        );
    }

    @Test
    void testTimeIsNotValidTimeForBooking() {
        when(orderRepository.findOrdersBySpecificDate(any())).thenReturn(ordersBySpecificDate);

        assertAll("is not valid",
                () -> assertFalse(dateTimeBookingService.isValidTimeForBooking(LocalDateTime.of(2023, 12, 1, 10, 0), LocalDateTime.of(2023, 12, 1, 11, 0)), "10-11"),
                () -> assertFalse(dateTimeBookingService.isValidTimeForBooking(LocalDateTime.of(2023, 12, 1, 12, 0), LocalDateTime.of(2023, 12, 1, 13, 0)), "12-13"),
                () -> assertFalse(dateTimeBookingService.isValidTimeForBooking(LocalDateTime.of(2023, 12, 1, 20, 0), LocalDateTime.of(2023, 12, 1, 23, 0)), "20-23"),
                () -> assertFalse(dateTimeBookingService.isValidTimeForBooking(LocalDateTime.of(2023, 12, 1, 5, 0), LocalDateTime.of(2023, 12, 1, 8, 0)), "5-8")
        );
    }

    @Test
    void testGetFreeDaysInMonth() {
        Set<LocalDate> expectedResult = Set.of(
                LocalDate.of(2023, 12, 1),
                LocalDate.of(2023, 12, 2),
                LocalDate.of(2023, 12, 3),
                LocalDate.of(2023, 12, 4),
                LocalDate.of(2023, 12, 6),
                LocalDate.of(2023, 12, 7),
                LocalDate.of(2023, 12, 8),
                LocalDate.of(2023, 12, 9),
                LocalDate.of(2023, 12, 11),
                LocalDate.of(2023, 12, 12),
                LocalDate.of(2023, 12, 13),
                LocalDate.of(2023, 12, 14),
                LocalDate.of(2023, 12, 16),
                LocalDate.of(2023, 12, 17),
                LocalDate.of(2023, 12, 18),
                LocalDate.of(2023, 12, 19),
                LocalDate.of(2023, 12, 21),
                LocalDate.of(2023, 12, 22),
                LocalDate.of(2023, 12, 23),
                LocalDate.of(2023, 12, 24),
                LocalDate.of(2023, 12, 26),
                LocalDate.of(2023, 12, 27),
                LocalDate.of(2023, 12, 28),
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31)
        );
        when(orderRepository.findOrdersByMonth(anyInt(), anyInt())).thenReturn(ordersByMonth);

        Set<LocalDate> actualResult = dateTimeBookingService.getFreeDaysForMonth(YearMonth.of(2023, 12));

        assertThat(actualResult).hasSameElementsAs(expectedResult);
    }
}