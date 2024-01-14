package com.rkukharenka.telegrambot.instaboxbot.bot.config;

import com.rkukharenka.telegrambot.instaboxbot.bot.BotFlowManager;
import com.rkukharenka.telegrambot.instaboxbot.bot.TelegramBot;
import com.rkukharenka.telegrambot.instaboxbot.common.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfig {

    @Bean
    public TelegramBot telegramBot(@Value("${telegram.bot.bot-username}") String botUsername,
                                   @Value("${telegram.bot.token}") String botToken,
                                   BotFlowManager botFlowManager,
                                   UserService userService) {
        return new TelegramBot(botUsername, botToken, botFlowManager, userService);
    }

}
