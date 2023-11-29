package com.rkukharenka.telegrambot.instaboxbot.service;

import com.rkukharenka.telegrambot.instaboxbot.entity.User;
import com.rkukharenka.telegrambot.instaboxbot.enums.ChatState;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UserService {

    void updateUser(User user);

    User getUser(Long chatId);

    User receiveOrRegisterUser(Long chatId, Update update);

    void changeChatState(User user, ChatState newChatState);

}
