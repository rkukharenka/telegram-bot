package com.rkukharenka.telegrambot.instaboxbot.web.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrderResponseDto {

    private Long orderId;
    private String username;
    private String phoneNumber;
    private String orderDate;
    private String orderTime;
    private String orderLocation;
    private String orderComment;

}
