package com.rkukharenka.telegrambot.instaboxbot.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface TelegramBotNotifier {

    void sendNotification(SendMessage sendMessage);

}
