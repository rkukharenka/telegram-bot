package com.rkukharenka.telegrambot.instaboxbot.bot;

import com.rkukharenka.telegrambot.instaboxbot.config.BotConfig;
import com.rkukharenka.telegrambot.instaboxbot.entity.User;
import com.rkukharenka.telegrambot.instaboxbot.service.SmartSender;
import com.rkukharenka.telegrambot.instaboxbot.service.UserService;
import com.rkukharenka.telegrambot.instaboxbot.utils.ChatUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final SmartSender smartSender;
    private final UserService userService;

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = ChatUtils.getChatId(update);

        User user = userService.receiveOrRegisterUser(chatId, update);

        smartSender.sendMessage(user, update).forEach(botApiMethod -> {
            try {
                execute(botApiMethod);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }
}
