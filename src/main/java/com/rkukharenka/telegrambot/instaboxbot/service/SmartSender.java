package com.rkukharenka.telegrambot.instaboxbot.service;

import com.rkukharenka.telegrambot.instaboxbot.entity.User;
import com.rkukharenka.telegrambot.instaboxbot.enums.ChatState;
import com.rkukharenka.telegrambot.instaboxbot.handler.RequestHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SmartSender {

    private final Map<ChatState, RequestHandler> handlersMap;

    public List<BotApiMethod<? extends Serializable>> sendMessage(User user, Update update) {
        return handlersMap.get(user.getChatState()).handleRequest(user, update);
    }


}
