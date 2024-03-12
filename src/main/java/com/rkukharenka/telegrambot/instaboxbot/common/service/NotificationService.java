package com.rkukharenka.telegrambot.instaboxbot.common.service;

public interface NotificationService {

    void sendNotificationToAdmin(String text);

    void sendNotificationToUser(Long chatId, String text);

}
