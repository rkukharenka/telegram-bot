package com.rkukharenka.telegrambot.instaboxbot.web.converter;

import com.rkukharenka.telegrambot.instaboxbot.common.entity.Order;
import com.rkukharenka.telegrambot.instaboxbot.common.entity.User;
import com.rkukharenka.telegrambot.instaboxbot.web.dto.OrderRequestDto;
import com.rkukharenka.telegrambot.instaboxbot.web.dto.OrderResponseDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.rkukharenka.telegrambot.instaboxbot.common.utils.DateTimeUtils.DATE_FORMAT;
import static com.rkukharenka.telegrambot.instaboxbot.common.utils.DateTimeUtils.TIME_FORMAT;

@Component
public class OrderConverter {

    public OrderResponseDto orderToResponseDto(Order order) {
        return new OrderResponseDto()
                .setOrderId(order.getId())
                .setUsername(order.getUser().getFirstName())
                .setPhoneNumber(order.getUser().getPhoneNumber())
                .setOrderDate(parseDate(order.getStartOrder()))
                .setOrderTime(parseTime(order.getStartOrder(), order.getFinishOrder()))
                .setOrderLocation(order.getLocation())
                .setOrderComment(order.getComment());
    }

    public Order requestDtoToOrder(OrderRequestDto orderRequestDto) {
        User user = new User()
                .setFirstName(orderRequestDto.getUsername())
                .setPhoneNumber(orderRequestDto.getPhoneNumber());
        return new Order()
                .setId(orderRequestDto.getOrderId())
                .setUser(user)
                .setStartOrder(LocalDateTime.of(orderRequestDto.getOrderDate(), orderRequestDto.getOrderTimeFrom()))
                .setFinishOrder(LocalDateTime.of(orderRequestDto.getOrderDate(), orderRequestDto.getOrderTimeTo()))
                .setLocation(orderRequestDto.getOrderLocation())
                .setComment(orderRequestDto.getOrderComment());
    }

    public OrderRequestDto orderToRequestDto(Order order) {
        return new OrderRequestDto()
                .setOrderId(order.getId())
                .setUsername(order.getUser().getFirstName())
                .setPhoneNumber(order.getUser().getPhoneNumber())
                .setOrderDate(LocalDate.from(order.getStartOrder()))
                .setOrderTimeFrom(LocalTime.from(order.getStartOrder()))
                .setOrderTimeTo(LocalTime.from(order.getFinishOrder()))
                .setOrderLocation(order.getLocation())
                .setOrderComment(order.getComment());
    }

    private String parseDate(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    private String parseTime(LocalDateTime from, LocalDateTime to) {
        return from.format(DateTimeFormatter.ofPattern(TIME_FORMAT)) +
               " - " +
               to.format(DateTimeFormatter.ofPattern(TIME_FORMAT));
    }

}
