package com.sparta.nationofdevelopment.domain.order.entity;

import com.sparta.nationofdevelopment.common_entity.Timestamped;
import com.sparta.nationofdevelopment.domain.user.entity.Users;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Cart extends Timestamped {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private long id;
    private int quantity;
    private int amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    Users user;

}
