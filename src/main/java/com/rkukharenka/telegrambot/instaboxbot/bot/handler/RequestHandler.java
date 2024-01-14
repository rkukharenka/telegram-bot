package com.rkukharenka.telegrambot.instaboxbot.bot.handler;

import com.rkukharenka.telegrambot.instaboxbot.common.entity.User;
import com.rkukharenka.telegrambot.instaboxbot.common.enums.ChatState;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.List;

public interface RequestHandler {

    boolean isApplicableRequest(Update update);

    List<BotApiMethod<? extends Serializable>> handleRequest(User user, Update update);

    ChatState getChatState();

}
