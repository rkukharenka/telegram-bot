package com.rkukharenka.telegrambot.instaboxbot.bot.handler.impl;

import com.rkukharenka.telegrambot.instaboxbot.bot.handler.RequestHandler;
import com.rkukharenka.telegrambot.instaboxbot.bot.helper.ChatUtils;
import com.rkukharenka.telegrambot.instaboxbot.common.entity.Order;
import com.rkukharenka.telegrambot.instaboxbot.common.entity.User;
import com.rkukharenka.telegrambot.instaboxbot.common.enums.ChatState;
import com.rkukharenka.telegrambot.instaboxbot.common.repository.OrderRepository;
import com.rkukharenka.telegrambot.instaboxbot.common.service.NotificationService;
import com.rkukharenka.telegrambot.instaboxbot.common.service.UserService;
import com.rkukharenka.telegrambot.instaboxbot.common.utils.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import static com.rkukharenka.telegrambot.instaboxbot.common.constant.CallbackDataConstant.CANCEL_ORDER;
import static com.rkukharenka.telegrambot.instaboxbot.common.constant.CallbackDataConstant.CREATE_ORDER;
import static com.rkukharenka.telegrambot.instaboxbot.common.constant.MessageConstant.*;

@Service
public class ConfirmCreateOrderHandlerImpl implements RequestHandler {

    private final UserService userService;
    private final OrderRepository orderRepository;

    private NotificationService notificationService;

    public ConfirmCreateOrderHandlerImpl(UserService userService, OrderRepository orderRepository) {
        this.userService = userService;
        this.orderRepository = orderRepository;
    }


    @Override
    public boolean isApplicableRequest(Update update) {
        return update.hasCallbackQuery() && StringUtils.isNotBlank(update.getCallbackQuery().getData());
    }

    @Override
    @Transactional
    public List<BotApiMethod<? extends Serializable>> handleRequest(User user, Update update) {
        if (CREATE_ORDER.equals(update.getCallbackQuery().getData())) {

            orderRepository.save(new Order()
                    .setUser(user)
                    .setComment(user.getComment())
                    .setLocation(user.getPreOrderInfo().getEventPlace())
                    .setStartOrder(LocalDateTime.of(user.getPreOrderDate(), user.getPreOrderStartTime()))
                    .setFinishOrder(LocalDateTime.of(user.getPreOrderDate(), user.getPreOrderEndTime())));


            notificationService.sendNotification(String.format(CREATE_ORDER_NOTIFICATION_MSG_FORMAT,
                    user.getFirstName(),
                    user.getPhoneNumber(),
                    DateTimeUtils.formatDateToString(user.getPreOrderDate()),
                    user.getPreOrderStartTime(), user.getPreOrderEndTime(),
                    user.getEventPlace(),
                    user.getComment()));

            userService.resetUserToInitialState(user);

            return List.of(getEditMessageReplyMarkup(user, update),
                    ChatUtils.createMessage(user.getChatId(),
                            String.format(CREATE_ORDER_MSG_FORMAT, user.getFirstName()),
                            null,
                            true));
        } else if (CANCEL_ORDER.equals(update.getCallbackQuery().getData())) {
            userService.resetUserToInitialState(user);

            return List.of(getEditMessageReplyMarkup(user, update), ChatUtils.createMessage(user.getChatId(),
                    CANCEL_ORDER_MSG_FORMAT,
                    null,
                    true));
        }
        return List.of();
    }

    private EditMessageReplyMarkup getEditMessageReplyMarkup(User user, Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        return new EditMessageReplyMarkup(String.valueOf(user.getChatId()),
                callbackQuery.getMessage().getMessageId(),
                callbackQuery.getInlineMessageId(),
                null);
    }

    @Override
    public ChatState getChatState() {
        return ChatState.CREATION_ORDER;
    }

    @Autowired
    public void setNotificationService(@Lazy NotificationService notificationService) {
        this.notificationService = notificationService;
    }
}
