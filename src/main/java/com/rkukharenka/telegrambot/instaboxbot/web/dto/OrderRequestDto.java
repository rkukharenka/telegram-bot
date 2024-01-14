package com.rkukharenka.telegrambot.instaboxbot.web.dto;

import com.rkukharenka.telegrambot.instaboxbot.common.utils.DateTimeUtils;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Accessors(chain = true)
public class OrderRequestDto {
    private Long orderId;

    @NotBlank(message = "Заполните имя")
    private String username;

    @NotBlank(message = "Укажите мобильный телефон")
    private String phoneNumber;

    @NotBlank(message = "Укажите дату")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate orderDate;

    @NotBlank(message = "Укажите время начала")
    @DateTimeFormat(pattern = DateTimeUtils.TIME_FORMAT)
    private LocalTime orderTimeFrom;

    @NotBlank(message = "Укажите время окончания")
    @DateTimeFormat(pattern = DateTimeUtils.TIME_FORMAT)
    private LocalTime orderTimeTo;

    @NotBlank(message = "Укажите место проведения мероприятия")
    private String orderLocation;

    private String orderComment;

}
