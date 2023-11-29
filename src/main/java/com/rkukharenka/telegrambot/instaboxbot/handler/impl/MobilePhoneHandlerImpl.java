package com.rkukharenka.telegrambot.instaboxbot.handler.impl;

import com.rkukharenka.telegrambot.instaboxbot.entity.User;
import com.rkukharenka.telegrambot.instaboxbot.enums.ChatState;
import com.rkukharenka.telegrambot.instaboxbot.handler.RequestHandler;
import com.rkukharenka.telegrambot.instaboxbot.service.UserService;
import com.rkukharenka.telegrambot.instaboxbot.utils.ChatUtils;
import com.rkukharenka.telegrambot.instaboxbot.utils.KeyboardFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.List;

import static com.rkukharenka.telegrambot.instaboxbot.constant.MessageConstant.ADD_COMMENT_OPTIONAL_MSG_FORMAT;
import static com.rkukharenka.telegrambot.instaboxbot.constant.MessageConstant.CHECK_NUMBER_PHONE_INPUT_MSG;

@Service
@RequiredArgsConstructor
public class MobilePhoneHandlerImpl implements RequestHandler {

    private final UserService userService;

    @Override
    @Transactional
    public List<BotApiMethod<? extends Serializable>> handleRequest(User user, Update update) {
        String mobilePhone = update.hasMessage() ? update.getMessage().getText() : StringUtils.EMPTY;

        if (!mobilePhone.matches("^(25|29|33|44)\\d{7}$")) {
            return List.of(ChatUtils.createMessage(user.getChatId(), CHECK_NUMBER_PHONE_INPUT_MSG, null, false));
        }

        user.setPhoneNumber(mobilePhone);
        user.setChatState(ChatState.ADDING_COMMENT_OPTIONAL);
        userService.updateUser(user);

        return List.of(ChatUtils.createMessage(user.getChatId(),
                String.format(ADD_COMMENT_OPTIONAL_MSG_FORMAT, user.getFirstName()),
                KeyboardFactory.getContinueOrAddComment(),
                false));
    }

    @Override
    public ChatState getChatState() {
        return ChatState.ADDING_MOBILE_PHONE;
    }

}