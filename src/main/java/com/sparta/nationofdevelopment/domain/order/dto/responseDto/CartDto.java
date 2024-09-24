package com.sparta.nationofdevelopment.domain.order.dto.responseDto;

import com.sparta.nationofdevelopment.domain.order.entity.Cart;
import lombok.Getter;

@Getter
public class CartDto {
    private final long cartId;
    private final long menuId;
    private final long storeId;
    private final String menuName;
    private final int quantity;
    private final int amount;
    private final long orderId;

    public CartDto(Cart cart) {
        this.cartId = cart.getId();
        this.menuId = cart.getMenu().getId();
        this.menuName = cart.getMenu().getMenuName();
        this.quantity = cart.getQuantity();
        this.amount = cart.getAmount();
        this.storeId = cart.getMenu().getStore().getStoreId();
        this.orderId = cart.getOrderId();
    }


}
