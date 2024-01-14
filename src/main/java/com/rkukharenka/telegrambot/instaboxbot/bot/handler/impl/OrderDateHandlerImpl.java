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
import com.rkukharenka.telegrambot.instaboxbot.common.utils.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderDateHandlerImpl implements RequestHandler {

    private final UserService userService;
    private final DateTimeBookingService dateTimeBookingService;

    @Override
    public boolean isApplicableRequest(Update update) {
        return update.hasCallbackQuery();
    }

    @Override
    @Transactional
    public List<BotApiMethod<? extends Serializable>> handleRequest(User user, Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        String callbackData = callbackQuery.getData();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        LocalDate preOrderDate = user.getPreOrderDate();

        updateCurrentDateIfNeed(user, preOrderDate);

        if (CallbackDataConstant.NEXT_MONTH.equals(callbackData)) {
            LocalDate nextMonth = user.getPreOrderDate().plusMonths(1);
            return processPreviousOrNextButton(nextMonth, user, callbackQuery);
        } else if (CallbackDataConstant.PREVIOUS_MONTH.equals(callbackData)) {
            LocalDate previousMonth = user.getPreOrderDate().minusMonths(1);
            return processPreviousOrNextButton(previousMonth, user, callbackQuery);
        } else if (DateTimeUtils.isDate(callbackData)) {
            return processDate(user, callbackData, messageId);
        }

        return List.of();
    }

    private void updateCurrentDateIfNeed(User user, LocalDate preOrderDate) {
        if (Objects.isNull(preOrderDate) || preOrderDate.isBefore(LocalDate.now())) {
            user.setPreOrderDate(LocalDate.now());
        }
    }

    private List<BotApiMethod<? extends Serializable>> processPreviousOrNextButton(LocalDate newPreOrderDate, User user,
                                                                                   CallbackQuery callbackQuery) {
        user.setPreOrderDate(newPreOrderDate);
        userService.updateUser(user);
        YearMonth selectMonth = YearMonth.from(user.getPreOrderDate());
        Set<LocalDate> freeDaysForMonth = dateTimeBookingService.getFreeDaysForMonth(selectMonth);

        return List.of(new EditMessageReplyMarkup(String.valueOf(user.getChatId()),
                callbackQuery.getMessage().getMessageId(),
                callbackQuery.getInlineMessageId(),
                (InlineKeyboardMarkup) KeyboardFactory.getCalendar(selectMonth, freeDaysForMonth)));
    }

    private List<BotApiMethod<? extends Serializable>> processDate(User user, String callbackData, Integer messageId) {
        user.setPreOrderDate(DateTimeUtils.formatStringToLocalDate(callbackData));
        userService.changeChatState(user, ChatState.SELECTING_TIME);

        DeleteMessage deleteMessage = new DeleteMessage(String.valueOf(user.getChatId()), messageId);

        SendMessage dateInfoMessage = ChatUtils.createMessage(user.getChatId(),
                String.format(MessageConstant.SHOW_PICKED_DATE_MSG_FORMAT, user.getFirstName(), callbackData),
                null,
                true);

        SendMessage pickDateMessage = ChatUtils.createMessage(user.getChatId(),
                String.format(MessageConstant.CHOOSE_TIME_MSG_FORMAT, getFreeTimeSlots(user.getPreOrderDate())),
                null,
                true);

        return List.of(deleteMessage, dateInfoMessage, pickDateMessage);
    }

    private String getFreeTimeSlots(LocalDate chosenDate) {
        List<LocalDateTime[]> freeTimeSlots = dateTimeBookingService.getFreeTimeSlotsBySpecificDate(chosenDate);
        return DateTimeUtils.freeTimeSlotsToString(freeTimeSlots);
    }

    @Override
    public ChatState getChatState() {
        return ChatState.SELECTING_DATE;
    }
}
