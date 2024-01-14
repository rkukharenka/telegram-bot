package com.rkukharenka.telegrambot.instaboxbot.bot.handler.impl;

import com.rkukharenka.telegrambot.instaboxbot.bot.handler.RequestHandler;
import com.rkukharenka.telegrambot.instaboxbot.bot.helper.ChatUtils;
import com.rkukharenka.telegrambot.instaboxbot.bot.helper.KeyboardFactory;
import com.rkukharenka.telegrambot.instaboxbot.common.constant.MessageConstant;
import com.rkukharenka.telegrambot.instaboxbot.common.entity.User;
import com.rkukharenka.telegrambot.instaboxbot.common.enums.ChatState;
import com.rkukharenka.telegrambot.instaboxbot.common.service.DateTimeBookingService;
import com.rkukharenka.telegrambot.instaboxbot.common.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GreetingHandlerImpl implements RequestHandler {

    private final UserService userService;
    private final DateTimeBookingService dateTimeBookingService;

    @Override
    public boolean isApplicableRequest(Update update) {
        return update.hasMessage() && "/start".equals(update.getMessage().getText());
    }

    @Override
    @Transactional
    public List<BotApiMethod<? extends Serializable>> handleRequest(User user, Update update) {
        Message message = update.getMessage();
        String greetingMessage = String.format(MessageConstant.GREETING_MSG_FORMAT, message.getFrom().getFirstName());

        userService.changeChatState(user, ChatState.SELECTING_DATE);

        YearMonth currentMonth = YearMonth.now();
        Set<LocalDate> freeDaysForMonth = dateTimeBookingService.getFreeDaysForMonth(currentMonth);

        return List.of(ChatUtils.createMessage(user.getChatId(), greetingMessage, null, false),
                ChatUtils.createMessage(user.getChatId(),
                        MessageConstant.CHOOSE_DATE_MSG,
                        KeyboardFactory.getCalendar(currentMonth, freeDaysForMonth),
                        false));
    }

    @Override
    public ChatState getChatState() {
        return ChatState.GREETING;
    }
}
