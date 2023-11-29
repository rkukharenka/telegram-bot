package com.rkukharenka.telegrambot.instaboxbot.handler.impl;

import com.rkukharenka.telegrambot.instaboxbot.constant.CallbackDataConstant;
import com.rkukharenka.telegrambot.instaboxbot.constant.MessageConstant;
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
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DateTimeConfirmationHandlerImpl implements RequestHandler {

    private final UserService userService;

    @Override
    @Transactional
    public List<BotApiMethod<? extends Serializable>> handleRequest(User user, Update update) {
        String callbackData = update.hasCallbackQuery() ? update.getCallbackQuery().getData() : StringUtils.EMPTY;
        if (CallbackDataConstant.ACCEPT_DATE_TIME.equals(callbackData)) {
            userService.changeChatState(user, ChatState.CHOOSING_EVENT_LOCATION);
            return List.of(deleteReviewMessage(user, update),
                    ChatUtils.createMessage(user.getChatId(), MessageConstant.ADD_EVENT_LOCATION_MSG, null, true));
        } else if (CallbackDataConstant.REPICK_DATE_TIME.equals(callbackData)) {
            userService.changeChatState(user, ChatState.SELECTING_DATE);
            return List.of(deleteReviewMessage(user, update),
                    ChatUtils.createMessage(user.getChatId(), MessageConstant.CHOOSE_DATE_MSG, KeyboardFactory.getCalendar(YearMonth.now()), false));
        }
        return List.of();
    }

    private DeleteMessage deleteReviewMessage(User user, Update update) {
        return new DeleteMessage(String.valueOf(user.getChatId()), update.getCallbackQuery().getMessage().getMessageId());
    }

    @Override
    public ChatState getChatState() {
        return ChatState.DATE_TIME_CONFIRMATION;
    }
}
