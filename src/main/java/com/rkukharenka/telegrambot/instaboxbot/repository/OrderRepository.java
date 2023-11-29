package com.rkukharenka.telegrambot.instaboxbot.repository;

import com.rkukharenka.telegrambot.instaboxbot.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
