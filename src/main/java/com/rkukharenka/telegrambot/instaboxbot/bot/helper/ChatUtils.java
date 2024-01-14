package com.rkukharenka.telegrambot.instaboxbot.bot.helper;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.Objects;

@Valid
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ChatUtils {

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

}
