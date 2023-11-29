package com.rkukharenka.telegrambot.instaboxbot.handler;

import com.rkukharenka.telegrambot.instaboxbot.entity.User;
import com.rkukharenka.telegrambot.instaboxbot.enums.ChatState;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.List;

public interface RequestHandler {

    List<BotApiMethod<? extends Serializable>> handleRequest(User user, Update update);

    ChatState getChatState();

}
