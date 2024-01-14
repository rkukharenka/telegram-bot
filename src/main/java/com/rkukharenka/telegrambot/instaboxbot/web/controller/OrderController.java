package com.rkukharenka.telegrambot.instaboxbot.web.controller;

import com.rkukharenka.telegrambot.instaboxbot.common.entity.Order;
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

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private static final String REDIRECT_TO_ORDERS = "redirect:/orders";
    private final OrderService orderService;
    private final OrderConverter orderConverter;

    @GetMapping("/orders")
    public String getOrders(Model model) {
        List<OrderResponseDto> orders = orderService.getAllOrders().stream()
                .map(orderConverter::orderToResponseDto)
                .toList();
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/create-order")
    public String showCreateOrderForm(OrderRequestDto orderRequestDto) {
        return "create-order";
    }

    @PostMapping("/createOrder")
    public String createOrder(@Valid OrderRequestDto orderRequestDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "create-order";
        }
        orderService.createOrder(orderConverter.requestDtoToOrder(orderRequestDto));

        return REDIRECT_TO_ORDERS;
    }

    @GetMapping("/deleteOrder/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return REDIRECT_TO_ORDERS;
    }

    @GetMapping("/edit-order/{id}")
    public String showEditOrder(@PathVariable Long id, Model model) {
        Order orderById = orderService.getOrderById(id);
        model.addAttribute("orderRequestDto", orderConverter.orderToRequestDto(orderById));
        return "update-order";
    }

    @PostMapping("/updateOrder/{id}")
    public String updateOrder(@PathVariable Long id, @Valid OrderRequestDto orderRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "update-order";
        }
        orderRequestDto.setOrderId(id);
        orderService.updateOrder(orderConverter.requestDtoToOrder(orderRequestDto));
        return REDIRECT_TO_ORDERS;
    }

}
