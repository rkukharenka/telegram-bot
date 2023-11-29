package com.rkukharenka.telegrambot.instaboxbot.utils;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ChatUtils {

    public static final String DATE_FORMAT = "dd-MM-yyyy";
    public static final String TIME_FORMAT = "HH:mm";
    public static final String MOBILE_PHONE_FORMAT = "+375(%s)%s-%s-%s";

    public static Long getChatId(Update update) {
        if (Objects.nonNull(update.getMessage())) {
            return update.getMessage().getChatId();
        } else if (Objects.nonNull(update.getCallbackQuery())) {
            return update.getCallbackQuery().getMessage().getChatId();
        } else {
            throw new IllegalStateException("Could not retrieve originating chat ID from update");
        }
    }

    public static User getUser(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getFrom();
        } else if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getFrom();
        } else {
            throw new IllegalStateException("Could not retrieve originating user from update");
        }
    }

    public static SendMessage createMessage(@NotNull Long chatId, @NotNull String text, ReplyKeyboard replyKeyboard, boolean enableHtml) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(replyKeyboard)
                .build();
        message.enableHtml(enableHtml);
        return message;
    }


    public static String formatPhoneNumber(String input) {
        String operatorCode = input.substring(0, 2);
        String firstPart = input.substring(2, 5);
        String secondPart = input.substring(5, 7);
        String thirdPart = input.substring(7);
        return String.format(MOBILE_PHONE_FORMAT, operatorCode, firstPart, secondPart, thirdPart);
    }

    public static String formatDateToString(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public static LocalDate formatStringToLocalDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public static boolean isDate(String text) {
        try {
            DateTimeFormatter.ofPattern(DATE_FORMAT).parse(text);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    public static LocalTime formatStringToLocalTime(String time) {
        return LocalTime.parse(time, DateTimeFormatter.ofPattern(TIME_FORMAT));
    }

}
