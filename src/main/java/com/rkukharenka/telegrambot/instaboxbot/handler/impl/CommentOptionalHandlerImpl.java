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
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.List;

import static com.rkukharenka.telegrambot.instaboxbot.constant.CallbackDataConstant.PRE_CREATE_ORDER;

@Service
@RequiredArgsConstructor
public class CommentOptionalHandlerImpl implements RequestHandler {

    private final UserService userService;

    @Override
    @Transactional
    public List<BotApiMethod<? extends Serializable>> handleRequest(User user, Update update) {
        Integer messageId;
        if (update.hasCallbackQuery() && PRE_CREATE_ORDER.equals(update.getCallbackQuery().getData())) {
            messageId = update.getCallbackQuery().getMessage().getMessageId();
            user.setComment(StringUtils.EMPTY);
        } else {
            messageId = update.getMessage().getMessageId();
            String comment = update.hasMessage() ? update.getMessage().getText() : StringUtils.EMPTY;
            user.setComment(comment);
        }

        user.setChatState(ChatState.CREATION_ORDER);
        userService.updateUser(user);

        return List.of(new DeleteMessage(String.valueOf(user.getChatId()), messageId),
                ChatUtils.createMessage(user.getChatId(),
                        String.format(MessageConstant.CONFIRM_CREATE_ORDER_MSG_FORMAT,
                                user.getPhoneNumber(),
                                user.getPreOrderDate(),
                                user.getPreOrderStartTime(), user.getPreOrderEndTime(),
                                user.getEventPlace()),
                        KeyboardFactory.getCreateOrderOrCancel(),
                        true));
    }

    @Override
    public ChatState getChatState() {
        return ChatState.ADDING_COMMENT_OPTIONAL;
    }
}
