package com.rkukharenka.telegrambot.instaboxbot.bot;

import com.rkukharenka.telegrambot.instaboxbot.bot.helper.ChatUtils;
import com.rkukharenka.telegrambot.instaboxbot.common.entity.User;
import com.rkukharenka.telegrambot.instaboxbot.common.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.Serializable;

@Slf4j
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot implements TelegramBotNotifier {

    private final String botUsername;
    private final String botToken;
    private final BotFlowManager botFlowManager;
    private final UserService userService;

    @PostConstruct
    public void initBot() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
        } catch (TelegramApiException e) {
            log.error("Error initializing Telegram Bot {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!isPrivateChat(update)) {
            return;
        }

        Long chatId = ChatUtils.getChatId(update);

        User user = userService.receiveOrRegisterUser(chatId, update);

        botFlowManager.generateMessages(user, update).forEach(this::sendMessage);
    }

    private boolean isPrivateChat(Update update) {
        return update.hasCallbackQuery() || (update.hasMessage() && update.getMessage().getChat().isUserChat());
    }

    private void sendMessage(BotApiMethod<? extends Serializable> botApiMethod) {
        try {
            execute(botApiMethod);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void sendNotification(SendMessage sendMessage) {
        sendMessage(sendMessage);
    }
}
