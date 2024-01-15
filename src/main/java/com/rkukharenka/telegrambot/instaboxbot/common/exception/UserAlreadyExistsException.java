package com.rkukharenka.telegrambot.instaboxbot.common.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(Long chatId) {
        super("User with chatId = %s already exists.".formatted(chatId));
    }
}
