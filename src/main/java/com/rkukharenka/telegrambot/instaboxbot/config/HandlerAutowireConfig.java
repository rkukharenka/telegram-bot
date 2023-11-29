package com.rkukharenka.telegrambot.instaboxbot.config;

import com.rkukharenka.telegrambot.instaboxbot.enums.ChatState;
import com.rkukharenka.telegrambot.instaboxbot.handler.RequestHandler;
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
