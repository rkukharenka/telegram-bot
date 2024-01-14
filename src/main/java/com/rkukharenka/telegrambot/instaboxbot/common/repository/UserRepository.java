package com.rkukharenka.telegrambot.instaboxbot.common.repository;

import com.rkukharenka.telegrambot.instaboxbot.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByChatId(Long chatId);

    Optional<User> findUserByPhoneNumber(String phoneNumber);

}
