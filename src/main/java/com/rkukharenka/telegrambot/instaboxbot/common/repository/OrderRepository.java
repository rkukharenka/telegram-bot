package com.rkukharenka.telegrambot.instaboxbot.common.repository;

import com.rkukharenka.telegrambot.instaboxbot.common.entity.Order;
import com.rkukharenka.telegrambot.instaboxbot.common.enums.OrderState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = """
            SELECT * 
            FROM orders 
            WHERE order_state IN ('NEW', 'ACCEPTED') AND DATE(start_order) = ?1 
            ORDER BY start_order
            """,
            nativeQuery = true)
    List<Order> findOrdersBySpecificDate(LocalDate localDate);

    @Query(value = """
            SELECT * 
            FROM orders 
            WHERE order_state IN ('NEW', 'ACCEPTED') AND EXTRACT(MONTH FROM start_order) = ?1 AND EXTRACT(YEAR FROM start_order) = ?2 
            ORDER BY start_order
            """,
            nativeQuery = true)
    List<Order> findOrdersByMonth(int month, int year);

    List<Order> findAllByOrderByStartOrderAsc();
    List<Order> findOrdersByOrderStateOrderByStartOrderAsc(OrderState orderState);

    List<Order> findOrdersByStartOrderAfterOrderByStartOrderAsc(LocalDateTime localDate);

    @Query(value = """
            SELECT o.*
            FROM orders o
                     INNER JOIN users u ON u.id = o.user_id
            WHERE UPPER(u.first_name) LIKE ?1 OR u.phone_number LIKE ?1
            ORDER BY o.start_order
            """, nativeQuery = true)
    List<Order> findOrdersByUserNameOrPhone(String keyword);


}
