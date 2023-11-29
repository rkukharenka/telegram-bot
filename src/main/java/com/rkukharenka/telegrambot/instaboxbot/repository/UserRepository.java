package com.rkukharenka.telegrambot.instaboxbot.repository;

import com.rkukharenka.telegrambot.instaboxbot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByChatId(Long chatId);

}
