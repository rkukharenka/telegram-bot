package com.rkukharenka.telegrambot.instaboxbot.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class BotConfig {

    @Value("${telegram.bot.bot-username}")
    private String botName;
    @Value("${telegram.bot.token}")
    private String token;

}
