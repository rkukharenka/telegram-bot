package com.rkukharenka.telegrambot.instaboxbot.common.service;

import com.rkukharenka.telegrambot.instaboxbot.common.entity.User;
import com.rkukharenka.telegrambot.instaboxbot.common.enums.ChatState;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public interface UserService {

    void updateUser(User user);

    User receiveOrRegisterUser(Long chatId, Update update);

    void changeChatState(User user, ChatState newChatState);

    void resetUserToInitialState(User user);

    void mergeUser(User manualUser, User telegramUser);

    Optional<User> getUserByPhoneNumber(String phoneNumber);

}
