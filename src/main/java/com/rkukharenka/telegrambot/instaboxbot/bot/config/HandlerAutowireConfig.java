package com.rkukharenka.telegrambot.instaboxbot.bot.config;

import com.rkukharenka.telegrambot.instaboxbot.bot.handler.RequestHandler;
import com.rkukharenka.telegrambot.instaboxbot.common.enums.ChatState;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class HandlerAutowireConfig {

    @Bean
    public Map<ChatState, RequestHandler> getHandlersMap(List<RequestHandler> handlers) {
        return handlers.stream()
                .collect(Collectors.toMap(RequestHandler::getChatState, Function.identity()));
    }

}
