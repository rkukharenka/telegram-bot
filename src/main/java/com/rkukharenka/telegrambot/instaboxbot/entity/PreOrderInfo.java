package com.rkukharenka.telegrambot.instaboxbot.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalTime;

@Embeddable
@Getter
@Setter
@Accessors(chain = true)
public class PreOrderInfo {

    private LocalDate preOrderDate;

    private LocalTime preOrderStartTime;

    private LocalTime preOrderEndTime;
    private String eventPlace;
    private String comment;

}
