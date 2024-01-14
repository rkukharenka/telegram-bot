package com.rkukharenka.telegrambot.instaboxbot.common.service.impl;

import com.rkukharenka.telegrambot.instaboxbot.common.entity.Order;
import com.rkukharenka.telegrambot.instaboxbot.common.entity.User;
import com.rkukharenka.telegrambot.instaboxbot.common.repository.OrderRepository;
import com.rkukharenka.telegrambot.instaboxbot.common.repository.UserRepository;
import com.rkukharenka.telegrambot.instaboxbot.common.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional
    public void createOrder(Order order) {
        String phoneNumber = order.getUser().getPhoneNumber();

        Optional<User> userByPhoneNumber = userRepository.findUserByPhoneNumber(phoneNumber);
        if (userByPhoneNumber.isPresent()) {
            order.setUser(userByPhoneNumber.get());
        } else {
            User newUser = userRepository.save(order.getUser());
            order.setUser(newUser);
        }

        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        Order orderById = getOrderById(orderId);
        orderRepository.delete(orderById);
    }

    @Override
    public Order getOrderById(Long orderId) {
        Optional<Order> orderById = orderRepository.findById(orderId);
        return orderById.orElseThrow(EntityNotFoundException::new);
    }

    @Override
    @Transactional
    public void updateOrder(Order order) {
        Order orderById = getOrderById(order.getId());
        User user = orderById.getUser();
        user.setFirstName(order.getUser().getFirstName());
        user.setPhoneNumber(order.getUser().getPhoneNumber());
        order.setUser(user);
        orderById.setComment(order.getComment());
        orderById.setLocation(order.getLocation());
        orderById.setStartOrder(order.getStartOrder());
        orderById.setFinishOrder(order.getFinishOrder());
        orderRepository.save(order);
    }

}
