package com.rkukharenka.telegrambot.instaboxbot.handler.impl;

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
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GreetingHandlerImpl implements RequestHandler {

    private final UserService userService;

    @Override
    @Transactional
    public List<BotApiMethod<? extends Serializable>> handleRequest(User user, Update update) {
        Message message = update.getMessage();
        String greetingMessage = String.format(MessageConstant.GREETING_MSG_FORMAT, message.getFrom().getFirstName());

        userService.changeChatState(user, ChatState.SELECTING_DATE);

        return List.of(ChatUtils.createMessage(user.getChatId(), greetingMessage, null, false),
                ChatUtils.createMessage(user.getChatId(), MessageConstant.CHOOSE_DATE_MSG, KeyboardFactory.getCalendar(YearMonth.now()), false));
    }

    @Override
    public ChatState getChatState() {
        return ChatState.GREETING;
    }
}
