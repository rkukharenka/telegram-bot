package com.rkukharenka.telegrambot.instaboxbot.handler.impl;

import com.rkukharenka.telegrambot.instaboxbot.entity.User;
import com.rkukharenka.telegrambot.instaboxbot.enums.ChatState;
import com.rkukharenka.telegrambot.instaboxbot.handler.RequestHandler;
import com.rkukharenka.telegrambot.instaboxbot.service.UserService;
import com.rkukharenka.telegrambot.instaboxbot.utils.ChatUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.List;

import static com.rkukharenka.telegrambot.instaboxbot.constant.MessageConstant.ADD_MOBILE_PHONE_FOR_ORDER_MSG;

@Service
@RequiredArgsConstructor
public class EventLocationHandlerImpl implements RequestHandler {

    private final UserService userService;

    @Override
    @Transactional
    public List<BotApiMethod<? extends Serializable>> handleRequest(User user, Update update) {
        String eventLocation = update.hasMessage() ? update.getMessage().getText() : StringUtils.EMPTY;
        user.setEventPlace(eventLocation);
        user.setChatState(ChatState.ADDING_MOBILE_PHONE);
        userService.updateUser(user);
        return List.of(ChatUtils.createMessage(user.getChatId(), ADD_MOBILE_PHONE_FOR_ORDER_MSG, null, false));
    }

    @Override
    public ChatState getChatState() {
        return ChatState.CHOOSING_EVENT_LOCATION;
    }
}
