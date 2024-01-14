package com.rkukharenka.telegrambot.instaboxbot.common.service.impl;

import com.rkukharenka.telegrambot.instaboxbot.bot.TelegramBotNotifier;
import com.rkukharenka.telegrambot.instaboxbot.bot.helper.ChatUtils;
import com.rkukharenka.telegrambot.instaboxbot.common.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
@RequiredArgsConstructor
public class TelegramNotificationServiceImpl implements NotificationService {

    private final TelegramBotNotifier telegramBotNotifier;

    @Override
    public void sendNotification(String text) {
        SendMessage message = ChatUtils.createMessage(-4040709811L, text, null, true);// TODO: 04-Jan-24
        telegramBotNotifier.sendNotification(message);
    }

}
