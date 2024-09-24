package com.sparta.nationofdevelopment.domain.order.service;


import com.sparta.nationofdevelopment.domain.common.dto.AuthUser;
import com.sparta.nationofdevelopment.domain.common.exception.ApiException;
import com.sparta.nationofdevelopment.domain.common.module.Finder;
import com.sparta.nationofdevelopment.domain.menu.entity.Menu;
import com.sparta.nationofdevelopment.domain.order.OrderStatus;
import com.sparta.nationofdevelopment.domain.order.dto.requestDto.OrderStatusRequestDto;
import com.sparta.nationofdevelopment.domain.order.dto.responseDto.OrderResponseDto;
import com.sparta.nationofdevelopment.domain.order.dto.responseDto.OrderStatusResponseDto;
import com.sparta.nationofdevelopment.domain.order.entity.Cart;
import com.sparta.nationofdevelopment.domain.order.entity.Orders;
import com.sparta.nationofdevelopment.domain.order.repository.CartRepository;
import com.sparta.nationofdevelopment.domain.order.repository.OrderRepository;
import com.sparta.nationofdevelopment.domain.store.entity.Store;
import com.sparta.nationofdevelopment.domain.store.entity.StoreStatus;
import com.sparta.nationofdevelopment.domain.user.entity.Users;
import com.sparta.nationofdevelopment.domain.user.enums.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class OrderServiceTest {
    @Mock
    OrderRepository orderRepository;
    @Mock
    CartRepository cartRepository;
    @Mock
    CartService cartService;
    @Mock
    Finder finder;
    @Mock
    Clock clock;

    @InjectMocks
    @Spy
    OrderService orderService;

    /**
     * 필요한 거
     * 유저 : 일반 유저, 가게 사장님
     * 가게 : 치킨집, 족발집
     * 메뉴 : 양념치킨, 순살치킨, 노말족발, 불족발
     */
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
    private Orders order1;
    private Orders order2;

    @BeforeEach
    void setUp() {
        //일반 유저
        user = new AuthUser(1L, "user@email.com", "일반유저", UserRole.USER);
        owner = new AuthUser(2L, "owner@email.com", "사장님", UserRole.OWNER);

        chickenStore = new Store(
                "치킨집"
                ,15000
                ,LocalTime.of(9, 00)
                ,LocalTime.of(23, 00)
                ,StoreStatus.OPEN
                ,Users.fromAuthUser(owner));
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
        ReflectionTestUtils.setField(cart1, "orderId", 1L);
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

        order2 = new Orders(36000,Users.fromAuthUser(user),jokbalStore,OrderStatus.WAITING);
        ReflectionTestUtils.setField(order2, "id", 2L);


    }

    @Test
    void create() {
        given(finder.findStoreByStoreId(anyLong())).willReturn(chickenStore);
        doNothing().when(orderService).validateIsStoreOpen(any());
        List<Cart> cartList = new ArrayList<>();
        cartList.add(cart1);
        cartList.add(cart2);
        given(cartRepository.findByUserIdAndOrderIdIsZero(any())).willReturn(cartList);
        given(orderRepository.save(any())).willReturn(order1);
        doNothing().when(cartService).saveOrderIdsToCarts(any(),any());

        OrderResponseDto response = orderService.create(user,1L);
        assertEquals(51000, response.getTotalAmount());
        assertEquals(2, response.getCartList().size());
        assertEquals(1L, response.getOrderId());
        assertEquals(1L, response.getUserId());
        assertEquals(1L, response.getStoreId());
        assertEquals(2,response.getCartList().size());
    }

    @Test
    void calculateTotalAmount() {
        List<Cart> cartList = new ArrayList<>();
        cartList.add(cart1);
        cartList.add(cart2);
        assertEquals(51000, orderService.calculateTotalAmount(cartList));
    }

    @Test
    void validateMinOrderAmount() {
        ApiException ex = assertThrows(ApiException.class,
                () -> orderService.validateMinOrderAmount(chickenStore, 5000));

        assertEquals("요청하신 주문금액이 가게 최소 주문금액보다 적습니다.", ex.getErrorCode().getReasonHttpStatus().getMessage());
    }

    @Test
    void validateIsStoreOpen() {
        Instant instant = LocalTime.of(3, 0).atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant();
        when(clock.instant()).thenReturn(instant);
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        ApiException ex = assertThrows(ApiException.class,
                () -> orderService.validateIsStoreOpen(chickenStore));
        assertEquals("현재 영업시간이 아닙니다.",ex.getErrorCode().getReasonHttpStatus().getMessage());
    }

    @Test
    void changeStatus() {
        OrderStatusRequestDto requestDto = new OrderStatusRequestDto();
        given(finder.findOrderByOrderId(anyLong())).willReturn(order1);
        doNothing().when(orderService).validateChangingOrderStatus(any(),any(),any());
        given(orderRepository.save(any())).willReturn(order1);
        ReflectionTestUtils.setField(requestDto,"status",OrderStatus.ACCEPTED);
        OrderStatusResponseDto response = orderService.changeStatus(user,1L,requestDto);
        assertEquals(OrderStatus.ACCEPTED,response.getStatus());
    }

    @Test
    void validateChangingOrderStatus() {
    }

    @Test
    void convertToOrderResponseDtos() {
        List<Orders> ordersList = new ArrayList<>();
        ordersList.add(order1);
        ordersList.add(order2);
        List<Cart> cartList = new ArrayList<>();
        cartList.add(cart3);
        cartList.add(cart4);
        given(cartRepository.findCartsByOrderIds(any())).willReturn(cartList);
        List<OrderResponseDto> response = orderService.convertToOrderResponseDtos(ordersList);
        assertEquals(36000, response.get(1).getTotalAmount());
        assertEquals(2, response.size());
        assertEquals(2L, response.get(1).getOrderId());
        assertEquals(1L, response.get(1).getUserId());
        assertEquals(2L, response.get(1).getStoreId());
        assertEquals(2,response.get(1).getCartList().size());
    }

    @Test
    void findByStoreId() {
        List<Orders> ordersList = new ArrayList<>();
        ordersList.add(order1);
        ordersList.add(order2);
        given(orderRepository.findByStore_StoreId(anyLong())).willReturn(ordersList);
        List<OrderResponseDto> response = orderService.findByStoreId(2L);
        assertEquals(36000, response.get(1).getTotalAmount());
        assertEquals(2, response.size());
        assertEquals(2L, response.get(1).getOrderId());
        assertEquals(1L, response.get(1).getUserId());
        assertEquals(2L, response.get(1).getStoreId());
    }

    @Test
    void findByUser() {
        List<Orders> ordersList = new ArrayList<>();
        ordersList.add(order1);
        ordersList.add(order2);
        given(orderRepository.findByUserId(anyLong())).willReturn(ordersList);
        List<OrderResponseDto> response = orderService.findByUser(user);
        assertEquals(36000, response.get(1).getTotalAmount());
        assertEquals(2, response.size());
        assertEquals(2L, response.get(1).getOrderId());
        assertEquals(1L, response.get(1).getUserId());
        assertEquals(2L, response.get(1).getStoreId());
    }

    @Test
    void findByOrderId() {
        given(finder.findOrderByOrderId(anyLong())).willReturn(order1);
        List<Cart> foundCart = new ArrayList<>();
        foundCart.add(cart1);
        foundCart.add(cart2);
        given(cartRepository.findByOrderId(1L)).willReturn(foundCart);

        OrderResponseDto responseDto = orderService.findByOrderId(1L);
        assertEquals(51000, responseDto.getTotalAmount());
        assertEquals(2, responseDto.getCartList().size());
        assertEquals(1L, responseDto.getOrderId());
        assertEquals(1L, responseDto.getUserId());
        assertEquals(1L, responseDto.getStoreId());
    }
}