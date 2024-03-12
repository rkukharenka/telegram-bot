package com.rkukharenka.telegrambot.instaboxbot.common.enums;

import lombok.Getter;

public enum OrderState {

    NEW("НОВЫЙ"),
    ACCEPTED("ПРИНЯТ"),
    DECLINED("ОТКЛОНЕН");

    @Getter
    private final String value;

    OrderState(String value) {
        this.value = value;
    }
}
