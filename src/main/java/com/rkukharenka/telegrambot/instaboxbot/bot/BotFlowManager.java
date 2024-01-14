package com.rkukharenka.telegrambot.instaboxbot.bot;

import com.rkukharenka.telegrambot.instaboxbot.bot.handler.RequestHandler;
import com.rkukharenka.telegrambot.instaboxbot.bot.helper.ChatUtils;
import com.rkukharenka.telegrambot.instaboxbot.common.constant.MessageConstant;
import com.rkukharenka.telegrambot.instaboxbot.common.entity.User;
import com.rkukharenka.telegrambot.instaboxbot.common.enums.ChatState;
import com.rkukharenka.telegrambot.instaboxbot.common.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class BotFlowManager {

    private final UserService userService;
    private final Map<ChatState, RequestHandler> handlersMap;

    public List<BotApiMethod<? extends Serializable>> generateMessages(User user, Update update) {

        applyCommands(user, update);

        RequestHandler requestHandler = handlersMap.get(user.getChatState());

        if (!requestHandler.isApplicableRequest(update)) {
            return List.of(ChatUtils.createMessage(user.getChatId(), MessageConstant.NOT_APPLICABLE_INPUT_MSG, null, false));
        }
        return requestHandler.handleRequest(user, update);
    }

    private void applyCommands(User user, Update update) {
        if (update.hasMessage() && "/start".equals(update.getMessage().getText()) && ChatState.GREETING != user.getChatState()) {
            userService.resetUserToInitialState(user);
        }
    }

}
