package com.rkukharenka.telegrambot.instaboxbot.exception;

public class TelegramUserNotFoundException extends RuntimeException {

    public TelegramUserNotFoundException(Long chatId) {
        super(String.format("User with chatId = %s not found.", chatId));
    }

}
