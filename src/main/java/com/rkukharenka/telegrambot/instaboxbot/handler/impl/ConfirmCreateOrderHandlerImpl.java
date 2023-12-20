package com.rkukharenka.telegrambot.instaboxbot.handler.impl;

import com.rkukharenka.telegrambot.instaboxbot.entity.Order;
import com.rkukharenka.telegrambot.instaboxbot.entity.PreOrderInfo;
import com.rkukharenka.telegrambot.instaboxbot.entity.User;
import com.rkukharenka.telegrambot.instaboxbot.enums.ChatState;
import com.rkukharenka.telegrambot.instaboxbot.handler.RequestHandler;
import com.rkukharenka.telegrambot.instaboxbot.repository.OrderRepository;
import com.rkukharenka.telegrambot.instaboxbot.service.UserService;
import com.rkukharenka.telegrambot.instaboxbot.utils.ChatUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import static com.rkukharenka.telegrambot.instaboxbot.constant.CallbackDataConstant.CREATE_ORDER;
import static com.rkukharenka.telegrambot.instaboxbot.constant.MessageConstant.CANCEL_ORDER_MSG_FORMAT;
import static com.rkukharenka.telegrambot.instaboxbot.constant.MessageConstant.CREATE_ORDER_MSG_FORMAT;

@Service
@RequiredArgsConstructor
public class ConfirmCreateOrderHandlerImpl implements RequestHandler {

    private final UserService userService;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public List<BotApiMethod<? extends Serializable>> handleRequest(User user, Update update) {
        if (update.hasCallbackQuery() && CREATE_ORDER.equals(update.getCallbackQuery().getData())) {

            orderRepository.save(new Order()
                    .setUser(user)
                    .setComment(user.getComment())
                    .setLocation(user.getPreOrderInfo().getEventPlace())
                    .setStartOrder(LocalDateTime.of(user.getPreOrderDate(), user.getPreOrderStartTime()))
                    .setFinishOrder(LocalDateTime.of(user.getPreOrderDate(), user.getPreOrderEndTime())));

            return List.of(getEditMessageReplyMarkup(user, update),
                    ChatUtils.createMessage(user.getChatId(),
                            String.format(CREATE_ORDER_MSG_FORMAT, user.getFirstName()),
                            null,
                            true));
        }
        user.setChatState(ChatState.GREETING).setPreOrderInfo(new PreOrderInfo());
        userService.updateUser(user);

        return List.of(getEditMessageReplyMarkup(user, update), ChatUtils.createMessage(user.getChatId(),
                CANCEL_ORDER_MSG_FORMAT,
                null,
                true));
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
}
