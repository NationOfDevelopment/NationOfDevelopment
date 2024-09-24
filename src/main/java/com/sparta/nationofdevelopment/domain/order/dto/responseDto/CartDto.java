package com.sparta.nationofdevelopment.domain.order.dto.responseDto;

import com.sparta.nationofdevelopment.domain.order.entity.Cart;
import lombok.Getter;

@Getter
public class CartDto {
    private final long orderId;
    private final long cartId;
    private final long menuId;
    private final String menuName;
    private final int quantity;
    private final int amount;

    public CartDto(Cart cart) {
        this.orderId = cart.getOrderId();
        this.cartId = cart.getId();
        this.menuId = cart.getMenu().getId();
        this.menuName = cart.getMenu().getMenuName();
        this.quantity = cart.getQuantity();
        this.amount = cart.getAmount();
    }


}
