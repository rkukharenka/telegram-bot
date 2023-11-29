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
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderDateHandlerImpl implements RequestHandler {

    private final UserService userService;

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
        } else if (ChatUtils.isDate(callbackData)) {
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
        return List.of(new EditMessageReplyMarkup(String.valueOf(user.getChatId()),
                callbackQuery.getMessage().getMessageId(),
                callbackQuery.getInlineMessageId(),
                (InlineKeyboardMarkup) KeyboardFactory.getCalendar(YearMonth.from(user.getPreOrderDate()))));
    }

    private List<BotApiMethod<? extends Serializable>> processDate(User user, String callbackData, Integer messageId) {
        user.setPreOrderDate(ChatUtils.formatStringToLocalDate(callbackData));
        userService.changeChatState(user, ChatState.SELECTING_TIME);

        DeleteMessage deleteMessage = new DeleteMessage(String.valueOf(user.getChatId()), messageId);

        SendMessage dateInfoMessage = ChatUtils.createMessage(user.getChatId(),
                String.format(MessageConstant.SHOW_PICKED_DATE_MSG_FORMAT, user.getFirstName(), callbackData),
                null,
                true);

        SendMessage pickDateMessage = ChatUtils.createMessage(user.getChatId(),
                MessageConstant.CHOOSE_TIME_MSG,
                null,
                true);

        return List.of(deleteMessage, dateInfoMessage, pickDateMessage);
    }

    @Override
    public ChatState getChatState() {
        return ChatState.SELECTING_DATE;
    }
}
