package com.rkukharenka.telegrambot.instaboxbot.common.service.impl;

import com.rkukharenka.telegrambot.instaboxbot.bot.helper.ChatUtils;
import com.rkukharenka.telegrambot.instaboxbot.common.entity.PreOrderInfo;
import com.rkukharenka.telegrambot.instaboxbot.common.entity.User;
import com.rkukharenka.telegrambot.instaboxbot.common.enums.ChatState;
import com.rkukharenka.telegrambot.instaboxbot.common.exception.UserAlreadyExistsException;
import com.rkukharenka.telegrambot.instaboxbot.common.repository.UserRepository;
import com.rkukharenka.telegrambot.instaboxbot.common.service.UserService;
import com.rkukharenka.telegrambot.instaboxbot.common.utils.PhoneNumberUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void updateUser(User user) {
        if (Objects.isNull(user.getChatId())) {
            throw new UserAlreadyExistsException(user.getChatId());
        }
        userRepository.save(user);
    }

    @Override
    public User receiveOrRegisterUser(Long chatId, Update update) {
        User user = userRepository.findUserByChatId(chatId).orElseGet(() -> registerNewUser(chatId, update));

        return Objects.isNull(user.getPreOrderInfo())
                ? user.setPreOrderInfo(new PreOrderInfo().setPreOrderDate(LocalDate.now()))
                : user;
    }

    @Override
    @Transactional
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

    @Override
    @Transactional
    public void resetUserToInitialState(User user) {
        user.setChatState(ChatState.GREETING).setPreOrderInfo(new PreOrderInfo());
        updateUser(user);
    }

    @Override
    @Transactional
    public void mergeUser(User manual, User telegram) {
        manual.setChatId(telegram.getChatId())
                .setChatState(telegram.getChatState())
                .setPreOrderInfo(new PreOrderInfo())
                .setPreOrderDate(telegram.getPreOrderDate())
                .setPreOrderStartTime(telegram.getPreOrderStartTime())
                .setPreOrderEndTime(telegram.getPreOrderEndTime())
                .setPreOrderEndTime(telegram.getPreOrderEndTime())
                .setEventPlace(telegram.getEventPlace())
                .setComment(telegram.getComment());

        userRepository.delete(telegram);
        userRepository.save(manual);
    }

    @Override
    public Optional<User> getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findUserByPhoneNumber(PhoneNumberUtils.removeNonNumeric(phoneNumber));
    }
}
