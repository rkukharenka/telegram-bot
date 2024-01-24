package com.rkukharenka.telegrambot.instaboxbot.common.service;

import com.rkukharenka.telegrambot.instaboxbot.common.entity.Order;

import java.util.List;

public interface OrderService {

    List<Order> getAllOrders();

    void createOrder(Order order);

    void declineOrder(Long orderId);

    Order getOrderById(Long orderId);

    void updateOrder(Order order);

}
