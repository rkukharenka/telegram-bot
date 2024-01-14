package com.rkukharenka.telegrambot.instaboxbot.bot.handler.impl;

import com.rkukharenka.telegrambot.instaboxbot.bot.handler.RequestHandler;
import com.rkukharenka.telegrambot.instaboxbot.bot.helper.ChatUtils;
import com.rkukharenka.telegrambot.instaboxbot.bot.helper.KeyboardFactory;
import com.rkukharenka.telegrambot.instaboxbot.common.constant.MessageConstant;
import com.rkukharenka.telegrambot.instaboxbot.common.entity.User;
import com.rkukharenka.telegrambot.instaboxbot.common.enums.ChatState;
import com.rkukharenka.telegrambot.instaboxbot.common.service.UserService;
import com.rkukharenka.telegrambot.instaboxbot.common.utils.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.List;

import static com.rkukharenka.telegrambot.instaboxbot.common.constant.CallbackDataConstant.PRE_CREATE_ORDER;

@Service
@RequiredArgsConstructor
public class CommentOptionalHandlerImpl implements RequestHandler {

    private final UserService userService;

    @Override
    public boolean isApplicableRequest(Update update) {
        return update.hasCallbackQuery() && StringUtils.isNotBlank(update.getCallbackQuery().getData())
               || update.hasMessage() && StringUtils.isNotBlank(update.getMessage().getText());
    }

    @Override
    @Transactional
    public List<BotApiMethod<? extends Serializable>> handleRequest(User user, Update update) {
        Integer messageId;
        if (update.hasCallbackQuery() && PRE_CREATE_ORDER.equals(update.getCallbackQuery().getData())) {
            messageId = update.getCallbackQuery().getMessage().getMessageId();
            user.setComment(StringUtils.EMPTY);
        } else {
            messageId = update.getMessage().getMessageId();
            String comment = update.getMessage().getText();
            user.setComment(comment);
        }

        user.setChatState(ChatState.CREATION_ORDER);
        userService.updateUser(user);

        return List.of(new DeleteMessage(String.valueOf(user.getChatId()), messageId),
                ChatUtils.createMessage(user.getChatId(),
                        String.format(MessageConstant.CONFIRM_CREATE_ORDER_MSG_FORMAT,
                                user.getPhoneNumber(),
                                DateTimeUtils.formatDateToString(user.getPreOrderDate()),
                                user.getPreOrderStartTime(), user.getPreOrderEndTime(),
                                user.getEventPlace(),
                                user.getComment()),
                        KeyboardFactory.getCreateOrderOrCancel(),
                        true));
    }

    @Override
    public ChatState getChatState() {
        return ChatState.ADDING_COMMENT_OPTIONAL;
    }
}
