package com.sparta.nationofdevelopment.domain.order.service;

import com.sparta.nationofdevelopment.domain.common.dto.AuthUser;
import com.sparta.nationofdevelopment.domain.common.module.Finder;
import com.sparta.nationofdevelopment.domain.menu.entity.Menu;
import com.sparta.nationofdevelopment.domain.order.OrderStatus;
import com.sparta.nationofdevelopment.domain.order.dto.responseDto.CartDto;
import com.sparta.nationofdevelopment.domain.order.entity.Cart;
import com.sparta.nationofdevelopment.domain.order.entity.Orders;
import com.sparta.nationofdevelopment.domain.order.repository.CartRepository;
import com.sparta.nationofdevelopment.domain.store.entity.Store;
import com.sparta.nationofdevelopment.domain.store.entity.StoreStatus;
import com.sparta.nationofdevelopment.domain.user.entity.Users;
import com.sparta.nationofdevelopment.domain.user.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
    @Mock
    CartRepository cartRepository;
    @Mock
    Finder finder;
    @InjectMocks
    CartService cartService;

    private AuthUser user;
    private AuthUser owner;
    private Store chickenStore;
    private Store jokbalStore;
    private Menu saucedChicken;
    private Menu friedChicken;
    private Menu normalJokbal;
    private Menu saucedJokbal;
    private Cart cart1;
    private Cart cart2;
    private Cart cart3;
    private Cart cart4;
    private List<Cart> chickenStoreCart = new ArrayList<>();
    private List<Cart> jokbalStoreCart = new ArrayList<>();
    private Orders order1;

    @BeforeEach
    void setUp() {
        //일반 유저
        user = new AuthUser(1L, "user@email.com", "일반유저", UserRole.USER);
        owner = new AuthUser(2L, "owner@email.com", "사장님", UserRole.OWNER);

        chickenStore = new Store(
                "치킨집"
                ,15000
                , LocalTime.of(9, 00)
                ,LocalTime.of(23, 00)
                , StoreStatus.OPEN
                , Users.fromAuthUser(owner));
        ReflectionTestUtils.setField(chickenStore,"storeId",1L);


        jokbalStore = new Store(
                "족발집"
                , 24000
                , LocalTime.of(9, 00)
                , LocalTime.of(23, 00)
                , StoreStatus.OPEN
                , Users.fromAuthUser(owner));
        ReflectionTestUtils.setField(jokbalStore,"storeId",2L);

        saucedChicken = new Menu();
        ReflectionTestUtils.setField(saucedChicken, "id", 1L);
        ReflectionTestUtils.setField(saucedChicken, "menuName", "양념치킨");
        ReflectionTestUtils.setField(saucedChicken, "amount", 18000);
        ReflectionTestUtils.setField(saucedChicken, "category", "치킨");
        ReflectionTestUtils.setField(saucedChicken, "store", chickenStore);

        friedChicken = new Menu();
        ReflectionTestUtils.setField(friedChicken, "id", 2L);
        ReflectionTestUtils.setField(friedChicken, "menuName", "후라이드치킨");
        ReflectionTestUtils.setField(friedChicken, "amount", 15000);
        ReflectionTestUtils.setField(friedChicken, "category", "치킨");
        ReflectionTestUtils.setField(friedChicken, "store", chickenStore);

        normalJokbal = new Menu();
        ReflectionTestUtils.setField(normalJokbal, "id", 3L);
        ReflectionTestUtils.setField(normalJokbal, "menuName", "노말족발");
        ReflectionTestUtils.setField(normalJokbal, "amount", 10000);
        ReflectionTestUtils.setField(normalJokbal, "category", "족발");
        ReflectionTestUtils.setField(normalJokbal, "store", jokbalStore);

        saucedJokbal = new Menu();
        ReflectionTestUtils.setField(saucedJokbal, "id", 4L);
        ReflectionTestUtils.setField(saucedJokbal, "menuName", "불족발");
        ReflectionTestUtils.setField(saucedJokbal, "amount", 13000);
        ReflectionTestUtils.setField(saucedJokbal, "category", "족발");
        ReflectionTestUtils.setField(saucedJokbal, "store", jokbalStore);


        cart1 = new Cart();
        ReflectionTestUtils.setField(cart1, "id", 1L);
        ReflectionTestUtils.setField(cart1, "menu", friedChicken);
        ReflectionTestUtils.setField(cart1, "quantity", 1);
        ReflectionTestUtils.setField(cart1, "amount", 15000);
        ReflectionTestUtils.setField(cart1, "user", Users.fromAuthUser(user));
        ReflectionTestUtils.setField(cart1, "orderId", 0);
        cart2 = new Cart();
        ReflectionTestUtils.setField(cart2, "id", 2L);
        ReflectionTestUtils.setField(cart2, "menu", saucedChicken);
        ReflectionTestUtils.setField(cart2, "quantity", 2);
        ReflectionTestUtils.setField(cart2, "amount", 36000);
        ReflectionTestUtils.setField(cart2, "user", Users.fromAuthUser(user));
        ReflectionTestUtils.setField(cart2, "orderId", 1L);
        cart3 = new Cart();
        ReflectionTestUtils.setField(cart3, "id", 3L);
        ReflectionTestUtils.setField(cart3, "menu", normalJokbal);
        ReflectionTestUtils.setField(cart3, "quantity", 1);
        ReflectionTestUtils.setField(cart3, "amount", 10000);
        ReflectionTestUtils.setField(cart3, "user", Users.fromAuthUser(owner));
        ReflectionTestUtils.setField(cart3,"orderId",2L);
        cart4 = new Cart();
        ReflectionTestUtils.setField(cart4, "id", 4L);
        ReflectionTestUtils.setField(cart4, "menu", saucedJokbal);
        ReflectionTestUtils.setField(cart4, "quantity", 2);
        ReflectionTestUtils.setField(cart4, "amount", 26000);
        ReflectionTestUtils.setField(cart4, "user", Users.fromAuthUser(owner));
        ReflectionTestUtils.setField(cart4,"orderId",2L);

        order1 = new Orders(51000, Users.fromAuthUser(user), chickenStore, OrderStatus.WAITING);
        ReflectionTestUtils.setField(order1, "id", 1L);

        chickenStoreCart.add(cart1);
        chickenStoreCart.add(cart2);
        jokbalStoreCart.add(cart3);
        jokbalStoreCart.add(cart4);
    }

    @Test
    void createCart() {
    }

    @Test
    void deleteCart() {
        doNothing().when(cartRepository).delete(any());
        given(finder.findCartByCartId(1L)).willReturn(cart1);

        String result = cartService.deleteCart(user,1L);

        verify(cartRepository,times(1)).delete(any());
        assertEquals("장바구니 삭제가 완료되었습니다.",result);
    }

    @Test
    void findCart() {
        given(cartRepository.findByUserIdAndOrderIdIsZero(any())).willReturn(chickenStoreCart);

        List<CartDto> response = cartService.findCart(user);
        assertEquals(2,response.size());
        assertEquals(1L,response.get(0).getCartId());
        assertEquals(1,response.get(0).getQuantity());
        assertEquals(15000,response.get(0).getAmount());
        assertEquals(1L,response.get(0).getCartId());
    }

    @Test
    void saveOrderIdsToCarts() {
        given(cartRepository.saveAll(any())).willReturn(chickenStoreCart);
        cartService.saveOrderIdsToCarts(chickenStoreCart,order1);
        verify(cartRepository,times(1)).saveAll(any());
        assertEquals(1L,chickenStoreCart.get(0).getOrderId());
        assertEquals(1L,chickenStoreCart.get(1).getOrderId());
    }
}