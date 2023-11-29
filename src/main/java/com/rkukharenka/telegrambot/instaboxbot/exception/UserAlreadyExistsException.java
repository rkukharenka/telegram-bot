package com.rkukharenka.telegrambot.instaboxbot.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(Long chatId) {
        super(String.format("User with chatId = %s already exists.", chatId));
    }
}
