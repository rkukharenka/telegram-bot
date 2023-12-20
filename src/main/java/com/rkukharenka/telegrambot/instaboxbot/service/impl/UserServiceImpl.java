package com.rkukharenka.telegrambot.instaboxbot.service.impl;

import com.rkukharenka.telegrambot.instaboxbot.entity.PreOrderInfo;
import com.rkukharenka.telegrambot.instaboxbot.entity.User;
import com.rkukharenka.telegrambot.instaboxbot.enums.ChatState;
import com.rkukharenka.telegrambot.instaboxbot.exception.TelegramUserNotFoundException;
import com.rkukharenka.telegrambot.instaboxbot.exception.UserAlreadyExistsException;
import com.rkukharenka.telegrambot.instaboxbot.repository.UserRepository;
import com.rkukharenka.telegrambot.instaboxbot.service.UserService;
import com.rkukharenka.telegrambot.instaboxbot.utils.ChatUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void updateUser(User user) {
        if (Objects.isNull(user.getChatId())) {
            throw new UserAlreadyExistsException(user.getChatId());
        }
        userRepository.save(user);
    }

    @Override
    public User getUser(Long chatId) {
        return userRepository.findById(chatId).orElseThrow(() -> new TelegramUserNotFoundException(chatId));
    }

    @Override
    public User receiveOrRegisterUser(Long chatId, Update update) {
        User user = userRepository.findUserByChatId(chatId).orElseGet(() -> registerNewUser(chatId, update));

        return Objects.isNull(user.getPreOrderInfo())
                ? user.setPreOrderInfo(new PreOrderInfo().setPreOrderDate(LocalDate.now()))
                : user;
    }

    @Override
    public void changeChatState(User user, ChatState newChatState) {
        userRepository.save(user.setChatState(newChatState));
    }

    private User registerNewUser(Long chatId, Update update) {
        User newUser = new User()
                .setChatId(chatId)
                .setFirstName(ChatUtils.getUser(update).getFirstName())
                .setChatState(ChatState.GREETING)
                .setPreOrderInfo(new PreOrderInfo());
        newUser.setPreOrderDate(LocalDate.now());
        return userRepository.save(newUser);
    }


}
