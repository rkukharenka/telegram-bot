package com.rkukharenka.telegrambot.instaboxbot.common.service.impl;

import com.rkukharenka.telegrambot.instaboxbot.bot.TelegramBotNotifier;
import com.rkukharenka.telegrambot.instaboxbot.bot.helper.ChatUtils;
import com.rkukharenka.telegrambot.instaboxbot.common.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
@RequiredArgsConstructor
public class TelegramNotificationServiceImpl implements NotificationService {

    private final TelegramBotNotifier telegramBotNotifier;
    @Value("${telegram.bot.admin-chat-id}")
    private String adminChatId;

    @Override
    public void sendNotificationToAdmin(String text) {
        SendMessage message = ChatUtils.createMessage(Long.valueOf(adminChatId), text, null, true);
        telegramBotNotifier.sendNotification(message);
    }

    @Override
    public void sendNotificationToUser(Long chatId, String text) {
        SendMessage message = ChatUtils.createMessage(chatId, text, null, true);
        telegramBotNotifier.sendNotification(message);
    }
}
