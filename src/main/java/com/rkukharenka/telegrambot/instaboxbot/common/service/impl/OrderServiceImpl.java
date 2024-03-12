package com.rkukharenka.telegrambot.instaboxbot.common.service.impl;

import com.rkukharenka.telegrambot.instaboxbot.common.constant.MessageConstant;
import com.rkukharenka.telegrambot.instaboxbot.common.entity.Order;
import com.rkukharenka.telegrambot.instaboxbot.common.entity.User;
import com.rkukharenka.telegrambot.instaboxbot.common.enums.OrderState;
import com.rkukharenka.telegrambot.instaboxbot.common.repository.OrderRepository;
import com.rkukharenka.telegrambot.instaboxbot.common.repository.UserRepository;
import com.rkukharenka.telegrambot.instaboxbot.common.service.NotificationService;
import com.rkukharenka.telegrambot.instaboxbot.common.service.OrderService;
import com.rkukharenka.telegrambot.instaboxbot.common.utils.DateTimeUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Value("${user-preferences.contact-mobile-phone}")
    private String adminMobilePhone;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByStartOrderAsc();
    }

    @Override
    public List<Order> getNewOrders() {
        return orderRepository.findOrdersByOrderStateOrderByStartOrderAsc(OrderState.NEW);
    }

    @Override
    public List<Order> getNextOrders() {
        return orderRepository.findOrdersByStartOrderAfterOrderByStartOrderAsc(LocalDateTime.now());
    }

    @Override
    public List<Order> getOrdersByUsernameOrPhoneNumber(String keyword) {
        return orderRepository.findOrdersByUserNameOrPhone("%%%s%%".formatted(keyword).toUpperCase());
    }

    @Override
    @Transactional
    public void createOrder(Order order, OrderState orderState) {
        String phoneNumber = order.getUser().getPhoneNumber();

        Optional<User> userByPhoneNumber = userRepository.findUserByPhoneNumber(phoneNumber);
        if (userByPhoneNumber.isPresent()) {
            order.setUser(userByPhoneNumber.get());
        } else {
            User newUser = userRepository.save(order.getUser());
            order.setUser(newUser);
        }

        order.setOrderState(orderState);
        order.setCreatedAt(LocalDateTime.now());

        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void declineOrder(Long orderId) {
        Order orderById = getOrderById(orderId);
        orderById.setOrderState(OrderState.DECLINED);
        orderRepository.save(orderById);

        notificationService.sendNotificationToUser(orderById.getUser().getChatId(), MessageConstant.ORDER_DECLINED_MSG);
    }

    @Override
    public void deleteOrder(Long orderId) {
        Order orderById = getOrderById(orderId);
        orderRepository.delete(orderById);
    }

    @Override
    @Transactional
    public void acceptOrder(Long orderId) {
        Order orderById = getOrderById(orderId);
        orderById.setOrderState(OrderState.ACCEPTED);
        orderRepository.save(orderById);

        String acceptMessage = MessageConstant.ORDER_ACCEPTED_MSG_FORMAT.formatted(
                orderById.getUser().getFirstName(),
                DateTimeUtils.formatDateToString(orderById.getStartOrder().toLocalDate()),
                DateTimeUtils.formatLocalTimeToString(orderById.getStartOrder()),
                DateTimeUtils.formatLocalTimeToString(orderById.getFinishOrder()),
                adminMobilePhone);
        notificationService.sendNotificationToUser(orderById.getUser().getChatId(), acceptMessage);
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
        orderRepository.save(orderById);
    }

}
