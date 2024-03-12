package com.rkukharenka.telegrambot.instaboxbot.common.exception;

public class TelegramUserNotFoundException extends RuntimeException {

    public TelegramUserNotFoundException(Long chatId) {
        super("User with chatId = %s not found.".formatted(chatId));
    }

    public TelegramUserNotFoundException(String message) {
        super("User with phoneNumber = %s not found.".formatted(message));
    }

}
