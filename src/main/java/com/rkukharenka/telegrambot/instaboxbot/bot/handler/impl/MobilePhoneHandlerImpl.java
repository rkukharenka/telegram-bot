package com.rkukharenka.telegrambot.instaboxbot.bot.handler.impl;

import com.rkukharenka.telegrambot.instaboxbot.bot.handler.RequestHandler;
import com.rkukharenka.telegrambot.instaboxbot.bot.helper.ChatUtils;
import com.rkukharenka.telegrambot.instaboxbot.bot.helper.KeyboardFactory;
import com.rkukharenka.telegrambot.instaboxbot.common.entity.User;
import com.rkukharenka.telegrambot.instaboxbot.common.enums.ChatState;
import com.rkukharenka.telegrambot.instaboxbot.common.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.rkukharenka.telegrambot.instaboxbot.common.constant.MessageConstant.ADD_COMMENT_OPTIONAL_MSG_FORMAT;

@Service
@RequiredArgsConstructor
public class MobilePhoneHandlerImpl implements RequestHandler {

    private final UserService userService;

    @Override
    public boolean isApplicableRequest(Update update) {
        return update.hasMessage()
               && Objects.nonNull(update.getMessage().getContact())
               && update.getMessage().getContact().getUserId().equals(update.getMessage().getChatId());
    }

    @Override
    @Transactional
    public List<BotApiMethod<? extends Serializable>> handleRequest(User user, Update update) {
        String mobilePhone = update.getMessage().getContact().getPhoneNumber();

        Optional<User> userByPhoneNumber = userService.getUserByPhoneNumber(mobilePhone);
        if(userByPhoneNumber.isPresent() && Objects.isNull(userByPhoneNumber.get().getChatId())){
            userService.mergeUser(userByPhoneNumber.get(), user);
            user = userByPhoneNumber.get();
        }

        user.setPhoneNumber(mobilePhone);
        user.setChatState(ChatState.ADDING_COMMENT_OPTIONAL);
        userService.updateUser(user);

        return List.of(ChatUtils.createMessage(user.getChatId(),
                String.format(ADD_COMMENT_OPTIONAL_MSG_FORMAT, user.getFirstName()),
                KeyboardFactory.getSkipComment(),
                false));
    }

    @Override
    public ChatState getChatState() {
        return ChatState.ADDING_MOBILE_PHONE;
    }

}