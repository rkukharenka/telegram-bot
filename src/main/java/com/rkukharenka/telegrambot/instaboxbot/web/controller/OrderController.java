package com.rkukharenka.telegrambot.instaboxbot.web.controller;

import com.rkukharenka.telegrambot.instaboxbot.common.entity.Order;
import com.rkukharenka.telegrambot.instaboxbot.common.enums.OrderState;
import com.rkukharenka.telegrambot.instaboxbot.common.service.OrderService;
import com.rkukharenka.telegrambot.instaboxbot.web.converter.OrderConverter;
import com.rkukharenka.telegrambot.instaboxbot.web.dto.OrderRequestDto;
import com.rkukharenka.telegrambot.instaboxbot.web.dto.OrderResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private static final String REDIRECT_TO_ORDERS = "redirect:/orders";
    private static final String CREATE_ORDER_VIEW = "create-order";
    private static final String UPDATE_ORDER_VIEW = "update-order";
    private final OrderService orderService;
    private final OrderConverter orderConverter;

    @GetMapping("/orders")
    public String getAllOrders(Model model) {
        return getOrders(orderService.getAllOrders(), model);
    }

    @GetMapping("/orders/search")
    public String getAllOrdersAfterSearch(@RequestParam String keyword, Model model) {
        return getOrders(orderService.getOrdersByUsernameOrPhoneNumber(keyword), model);
    }

    @GetMapping("/orders/new")
    public String getNewOrders(Model model) {
        return getOrders(orderService.getNewOrders(), model);
    }

    @GetMapping("/orders/next")
    public String getNextOrders(Model model) {
        return getOrders(orderService.getNextOrders(), model);
    }

    private String getOrders(List<Order> orders, Model model) {
        List<OrderResponseDto> dtoList = orders.stream()
                .map(orderConverter::orderToResponseDto)
                .toList();
        model.addAttribute("orders", dtoList);
        return "orders";
    }

    @GetMapping("/create-order")
    public String showCreateOrderForm(OrderRequestDto orderRequestDto) {
        return CREATE_ORDER_VIEW;
    }

    @PostMapping("/createOrder")
    public String createOrder(@Valid OrderRequestDto orderRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return CREATE_ORDER_VIEW;
        }
        orderService.createOrder(orderConverter.requestDtoToOrder(orderRequestDto), OrderState.ACCEPTED);

        return REDIRECT_TO_ORDERS;
    }

    @GetMapping("/orders/{id}/decline")
    public String declineOrder(@PathVariable Long id) {
        orderService.declineOrder(id);
        return REDIRECT_TO_ORDERS;
    }

    @GetMapping("/orders/{id}/delete")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return REDIRECT_TO_ORDERS;
    }

    @GetMapping("/orders/{id}/accept")
    public String acceptOrder(@PathVariable Long id) {
        orderService.acceptOrder(id);
        return REDIRECT_TO_ORDERS;
    }

    @GetMapping("/orders/{id}/edit")
    public String showEditOrder(@PathVariable Long id, Model model) {
        Order orderById = orderService.getOrderById(id);
        model.addAttribute("orderRequestDto", orderConverter.orderToRequestDto(orderById));
        return UPDATE_ORDER_VIEW;
    }

    @PostMapping("/orders/{id}/update")
    public String updateOrder(@PathVariable Long id, @Valid OrderRequestDto orderRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return UPDATE_ORDER_VIEW;
        }
        orderRequestDto.setOrderId(id);

        orderService.updateOrder(orderConverter.requestDtoToOrder(orderRequestDto));
        return REDIRECT_TO_ORDERS;
    }

}
