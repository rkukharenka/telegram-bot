package com.rkukharenka.telegrambot.instaboxbot.bot.handler.impl;

import com.rkukharenka.telegrambot.instaboxbot.bot.handler.RequestHandler;
import com.rkukharenka.telegrambot.instaboxbot.bot.helper.ChatUtils;
import com.rkukharenka.telegrambot.instaboxbot.bot.helper.KeyboardFactory;
import com.rkukharenka.telegrambot.instaboxbot.common.constant.MessageConstant;
import com.rkukharenka.telegrambot.instaboxbot.common.entity.User;
import com.rkukharenka.telegrambot.instaboxbot.common.enums.ChatState;
import com.rkukharenka.telegrambot.instaboxbot.common.service.DateTimeBookingService;
import com.rkukharenka.telegrambot.instaboxbot.common.service.UserService;
import com.rkukharenka.telegrambot.instaboxbot.common.utils.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderTimeHandlerImpl implements RequestHandler {

    public static final String TIME_SEPARATOR = "-";
    private final UserService userService;
    private final DateTimeBookingService dateTimeBookingService;

    @Override
    public boolean isApplicableRequest(Update update) {
        return update.hasMessage() && StringUtils.isNotBlank(update.getMessage().getText());
    }

    @Override
    @Transactional
    public List<BotApiMethod<? extends Serializable>> handleRequest(User user, Update update) {
        String timeString = update.getMessage().getText();


        if (!isValidTimePeriod(timeString)) {
            return List.of(ChatUtils.createMessage(user.getChatId(),
                    MessageConstant.REPICK_TIME_MSG,
                    null,
                    true));
        }

        String[] timePeriod = timeString.split(TIME_SEPARATOR);
        LocalTime startTime = DateTimeUtils.formatStringToLocalTime(timePeriod[0]);
        LocalTime endTime = DateTimeUtils.formatStringToLocalTime(timePeriod[1]);

        if (!dateTimeBookingService.isValidTimeForBooking(LocalDateTime.of(user.getPreOrderDate(), startTime),
                LocalDateTime.of(user.getPreOrderDate(), endTime))) {
            return List.of(ChatUtils.createMessage(user.getChatId(),
                    String.format(MessageConstant.CHOSEN_TIME_BUSY_MSG_FORMAT, getFreeTimeSlots(user.getPreOrderDate())),
                    null,
                    true));
        }

        user.setPreOrderStartTime(startTime).setPreOrderEndTime(endTime);
        user.setChatState(ChatState.DATE_TIME_CONFIRMATION);
        userService.updateUser(user);

        SendMessage pickTimeMessage = ChatUtils.createMessage(user.getChatId(),
                String.format(MessageConstant.SHOW_PICKED_TIME_MSG_FORMAT, user.getFirstName(), timePeriod[0], timePeriod[1]),
                null,
                true);

        SendMessage confirmationMessage = ChatUtils.createMessage(user.getChatId(),
                String.format(MessageConstant.REVIEW_DATE_AND_TIME_PREORDER_MSG,
                        DateTimeUtils.formatDateToString(user.getPreOrderDate()), timePeriod[0], timePeriod[1]),
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
            startTime = DateTimeUtils.formatStringToLocalTime(timePeriod[0]);
            endTime = DateTimeUtils.formatStringToLocalTime(timePeriod[1]);
        } catch (DateTimeParseException e) {
            return false;
        }

        return ChronoUnit.MINUTES.between(startTime, endTime) >= 60 && startTime.isBefore(endTime);
    }

    private String getFreeTimeSlots(LocalDate chosenDate) {
        List<LocalDateTime[]> freeTimeSlots = dateTimeBookingService.getFreeTimeSlotsBySpecificDate(chosenDate);
        return DateTimeUtils.freeTimeSlotsToString(freeTimeSlots);
    }

}
