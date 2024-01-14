package com.rkukharenka.telegrambot.instaboxbot.bot.handler.impl;

import com.rkukharenka.telegrambot.instaboxbot.bot.handler.RequestHandler;
import com.rkukharenka.telegrambot.instaboxbot.bot.helper.ChatUtils;
import com.rkukharenka.telegrambot.instaboxbot.common.entity.User;
import com.rkukharenka.telegrambot.instaboxbot.common.enums.ChatState;
import com.rkukharenka.telegrambot.instaboxbot.common.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.Serializable;
import java.util.List;

import static com.rkukharenka.telegrambot.instaboxbot.common.constant.MessageConstant.ADD_MOBILE_PHONE_FOR_ORDER_MSG;

@Service
@RequiredArgsConstructor
public class EventLocationHandlerImpl implements RequestHandler {

    private final UserService userService;

    @Override
    public boolean isApplicableRequest(Update update) {
        return update.hasMessage() && StringUtils.isNotBlank(update.getMessage().getText());
    }

    @Override
    @Transactional
    public List<BotApiMethod<? extends Serializable>> handleRequest(User user, Update update) {
        String eventLocation = update.getMessage().getText();
        user.setEventPlace(eventLocation);
        user.setChatState(ChatState.ADDING_MOBILE_PHONE);
        userService.updateUser(user);
        return List.of(ChatUtils.createMessage(user.getChatId(),
                ADD_MOBILE_PHONE_FOR_ORDER_MSG,
                getReplyKeyboardForPhoneRequest(),
                false));
    }

    private ReplyKeyboardMarkup getReplyKeyboardForPhoneRequest() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        KeyboardButton requestPhoneButton = new KeyboardButton("Поделиться номером телефона");
        requestPhoneButton.setRequestContact(true);
        keyboardMarkup.setKeyboard(List.of(new KeyboardRow(List.of(requestPhoneButton))));
        keyboardMarkup.setOneTimeKeyboard(true);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    @Override
    public ChatState getChatState() {
        return ChatState.CHOOSING_EVENT_LOCATION;
    }

}
