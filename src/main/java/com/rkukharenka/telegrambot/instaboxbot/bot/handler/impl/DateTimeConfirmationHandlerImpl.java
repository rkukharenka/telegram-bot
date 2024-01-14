package com.rkukharenka.telegrambot.instaboxbot.bot.handler.impl;

import com.rkukharenka.telegrambot.instaboxbot.bot.handler.RequestHandler;
import com.rkukharenka.telegrambot.instaboxbot.bot.helper.ChatUtils;
import com.rkukharenka.telegrambot.instaboxbot.bot.helper.KeyboardFactory;
import com.rkukharenka.telegrambot.instaboxbot.common.constant.CallbackDataConstant;
import com.rkukharenka.telegrambot.instaboxbot.common.constant.MessageConstant;
import com.rkukharenka.telegrambot.instaboxbot.common.entity.User;
import com.rkukharenka.telegrambot.instaboxbot.common.enums.ChatState;
import com.rkukharenka.telegrambot.instaboxbot.common.service.DateTimeBookingService;
import com.rkukharenka.telegrambot.instaboxbot.common.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DateTimeConfirmationHandlerImpl implements RequestHandler {

    private final UserService userService;
    private final DateTimeBookingService dateTimeBookingService;

    @Override
    public boolean isApplicableRequest(Update update) {
        return update.hasCallbackQuery() && StringUtils.isNotBlank(update.getCallbackQuery().getData());
    }

    @Override
    @Transactional
    public List<BotApiMethod<? extends Serializable>> handleRequest(User user, Update update) {
        String callbackData = update.getCallbackQuery().getData();
        if (CallbackDataConstant.ACCEPT_DATE_TIME.equals(callbackData)) {
            userService.changeChatState(user, ChatState.CHOOSING_EVENT_LOCATION);

            return List.of(deleteReviewMessage(user, update),
                    ChatUtils.createMessage(user.getChatId(),
                            MessageConstant.ADD_EVENT_LOCATION_MSG,
                            null,
                            true));
        } else if (CallbackDataConstant.REPICK_DATE_TIME.equals(callbackData)) {
            userService.changeChatState(user, ChatState.SELECTING_DATE);
            YearMonth currentMonth = YearMonth.now();
            Set<LocalDate> freeDaysForMonth = dateTimeBookingService.getFreeDaysForMonth(currentMonth);

            return List.of(deleteReviewMessage(user, update),
                    ChatUtils.createMessage(user.getChatId(),
                            MessageConstant.CHOOSE_DATE_MSG,
                            KeyboardFactory.getCalendar(currentMonth, freeDaysForMonth),
                            false));
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
