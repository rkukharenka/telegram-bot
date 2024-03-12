package com.rkukharenka.telegrambot.instaboxbot.common.entity;

import com.rkukharenka.telegrambot.instaboxbot.common.enums.ChatState;
import com.rkukharenka.telegrambot.instaboxbot.common.enums.OrderState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Accessors(chain = true)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "order_state")
    @Enumerated(EnumType.STRING)
    private OrderState orderState;

    @Column(name = "comment")
    private String comment;

    @Column(name = "location")
    private String location;

    @Column(name = "start_order")
    private LocalDateTime startOrder;

    @Column(name = "finish_order")
    private LocalDateTime finishOrder;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return new EqualsBuilder().append(id, order.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).toHashCode();
    }
}