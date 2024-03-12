package com.rkukharenka.telegrambot.instaboxbot.common.service;

import com.rkukharenka.telegrambot.instaboxbot.common.entity.Order;
import com.rkukharenka.telegrambot.instaboxbot.common.enums.OrderState;

import java.util.List;

public interface OrderService {

    List<Order> getAllOrders();

    List<Order> getNewOrders();

    List<Order> getNextOrders();

    List<Order> getOrdersByUsernameOrPhoneNumber(String keyword);

    void createOrder(Order order, OrderState orderState);

    void declineOrder(Long orderId);
    void deleteOrder(Long orderId);

    void acceptOrder(Long orderId);

    Order getOrderById(Long orderId);

    void updateOrder(Order order);

}
