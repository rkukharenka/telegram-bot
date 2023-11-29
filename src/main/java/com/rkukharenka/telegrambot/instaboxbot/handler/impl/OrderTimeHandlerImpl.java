package com.rkukharenka.telegrambot.instaboxbot.handler.impl;

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
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderTimeHandlerImpl implements RequestHandler {

    public static final String TIME_SEPARATOR = "-";
    private final UserService userService;

    @Override
    @Transactional
    public List<BotApiMethod<? extends Serializable>> handleRequest(User user, Update update) {
        String timeString = update.hasMessage() ? update.getMessage().getText() : StringUtils.EMPTY;

        if (!isValidTimePeriod(timeString)) {
            return List.of(ChatUtils.createMessage(user.getChatId(),
                    MessageConstant.REPICK_TIME_MSG,
                    null,
                    true));
        }

        String[] timePeriod = timeString.split(TIME_SEPARATOR);
        LocalTime startTime = ChatUtils.formatStringToLocalTime(timePeriod[0]);
        LocalTime endTime = ChatUtils.formatStringToLocalTime(timePeriod[1]);
        user.setPreOrderStartTime(startTime).setPreOrderEndTime(endTime);
        user.setChatState(ChatState.DATE_TIME_CONFIRMATION);
        userService.updateUser(user);

        SendMessage pickTimeMessage = ChatUtils.createMessage(user.getChatId(),
                String.format(MessageConstant.SHOW_PICKED_TIME_MSG_FORMAT, user.getFirstName(), timePeriod[0], timePeriod[1]),
                null,
                true);

        SendMessage confirmationMessage = ChatUtils.createMessage(user.getChatId(),
                String.format(MessageConstant.REVIEW_DATE_AND_TIME_PREORDER_MSG,
                        ChatUtils.formatDateToString(user.getPreOrderDate()), timePeriod[0], timePeriod[1]),
                KeyboardFactory.getContinueOrRepickDateTime(),
                true);

        return List.of(pickTimeMessage, confirmationMessage);
    }

    @Override
    public ChatState getChatState() {
        return ChatState.SELECTING_TIME;
    }

    private boolean isValidTimePeriod(String timeFromTo) {
        String[] timePeriod = timeFromTo.split(TIME_SEPARATOR);
        if (timePeriod.length != 2) {
            return false;
        }

        LocalTime startTime;
        LocalTime endTime;
        try {
            startTime = ChatUtils.formatStringToLocalTime(timePeriod[0]);
            endTime = ChatUtils.formatStringToLocalTime(timePeriod[1]);
        } catch (DateTimeParseException e) {
            return false;
        }

        return ChronoUnit.MINUTES.between(startTime, endTime) >= 60 && startTime.isBefore(endTime);
    }
}
