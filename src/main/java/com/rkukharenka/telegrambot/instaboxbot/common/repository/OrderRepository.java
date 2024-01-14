package com.rkukharenka.telegrambot.instaboxbot.common.repository;

import com.rkukharenka.telegrambot.instaboxbot.common.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "SELECT * " +
                   "FROM orders " +
                   "WHERE FORMATDATETIME(start_order, 'yyyy-MM-dd') = ?1 ORDER BY start_order",
            nativeQuery = true)
    List<Order> findOrdersBySpecificDate(LocalDate localDate);

    @Query(value = "SELECT * " +
                   "FROM orders " +
                   "WHERE EXTRACT(MONTH FROM start_order) = ?1 AND EXTRACT(YEAR FROM start_order) = ?2 " +
                   "ORDER BY start_order",
            nativeQuery = true)
    List<Order> findOrdersByMonth(int month, int year);

}
