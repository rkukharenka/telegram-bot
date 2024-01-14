package com.rkukharenka.telegrambot.instaboxbot.common.entity;

import com.rkukharenka.telegrambot.instaboxbot.common.enums.ChatState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.Delegate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "chat_state")
    @Enumerated(EnumType.STRING)
    private ChatState chatState;

    @Embedded
    @Delegate
    @AttributeOverrides({
            @AttributeOverride(name = "preOrderDate", column = @Column(name = "pre_order_date")),
            @AttributeOverride(name = "preOrderStartTime", column = @Column(name = "pre_order_start_time")),
            @AttributeOverride(name = "preOrderEndTime", column = @Column(name = "pre_order_end_time")),
            @AttributeOverride(name = "eventPlace", column = @Column(name = "event_place")),
            @AttributeOverride(name = "comment", column = @Column(name = "comment"))
    })
    private PreOrderInfo preOrderInfo;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL})
    private List<Order> orders = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return new EqualsBuilder().append(chatId, user.chatId).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(chatId).toHashCode();
    }
}
