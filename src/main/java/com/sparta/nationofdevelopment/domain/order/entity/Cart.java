package com.sparta.nationofdevelopment.domain.order.entity;

import com.sparta.nationofdevelopment.common_entity.Timestamped;
import com.sparta.nationofdevelopment.domain.common.dto.AuthUser;
import com.sparta.nationofdevelopment.domain.menu.entity.Menu;
import com.sparta.nationofdevelopment.domain.order.dto.MenuItemDto;
import com.sparta.nationofdevelopment.domain.user.entity.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "cart")
public class Cart extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private long id;
    private int quantity;
    private int amount;
    private long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    Menu menu;

    public Cart(MenuItemDto dto, AuthUser authUser, Menu menu) {
        this.quantity = dto.getQuantity();
        this.amount = menu.getAmount()*dto.getQuantity();
        this.user = Users.fromAuthUser(authUser);
        this.menu = menu;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
}
