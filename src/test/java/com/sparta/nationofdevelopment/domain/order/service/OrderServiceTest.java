package com.sparta.nationofdevelopment.domain.order.service;

import com.sparta.nationofdevelopment.domain.common.dto.AuthUser;
import com.sparta.nationofdevelopment.domain.menu.entity.Menu;
import com.sparta.nationofdevelopment.domain.menu.repository.MenuRepository;
import com.sparta.nationofdevelopment.domain.order.OrderStatus;
import com.sparta.nationofdevelopment.domain.order.dto.MenuItemDto;
import com.sparta.nationofdevelopment.domain.order.dto.OrderRequestDto;
import com.sparta.nationofdevelopment.domain.order.dto.OrderResponseDto;
import com.sparta.nationofdevelopment.domain.order.entity.Cart;
import com.sparta.nationofdevelopment.domain.order.repository.OrderRepository;
import com.sparta.nationofdevelopment.domain.store.entity.Store;
import com.sparta.nationofdevelopment.domain.store.repository.StoreRepository;
import com.sparta.nationofdevelopment.domain.user.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    OrderRepository orderRepository;
    @Mock
    StoreRepository storeRepository;
    @Mock
    MenuRepository menuRepository;
    @Spy
    @InjectMocks
    OrderService orderService;

    /**
     * returnToCartList(임시)
     * requestDto : menuItemDto 리스트
     * menuItemDto : String menuName, int quantity
     * AuthUser 필요,
     * 메뉴,스토어,유저 있다고 치고
     */
    private AuthUser testUser;
    private Menu friedChicken;
    private Menu saucedChicken;
    private Store store;

    @BeforeEach
    void setUp() {
        testUser = new AuthUser(1L,"tester@email.com","테스터1", UserRole.USER);
        friedChicken = new Menu();
        friedChicken.setId(1L);
        friedChicken.setManuName("후라이드");
        friedChicken.setAmount(15000);

        saucedChicken = new Menu();
        saucedChicken.setId(2L);
        saucedChicken.setManuName("양념");
        saucedChicken.setAmount(18000);

        store = new Store();
        store.setId(1L);

    }

    @Test
    void 장바구니_생성_테스트() {
        OrderRequestDto requestDto = new OrderRequestDto();
        List<MenuItemDto> menuItems = new ArrayList<>();
        MenuItemDto fried = new MenuItemDto("후라이드",1);
        menuItems.add(fried);
        MenuItemDto sauced = new MenuItemDto("양념", 2);
        menuItems.add(sauced);
        ReflectionTestUtils.setField(requestDto,"menuItems",menuItems);
        given(menuRepository.findByMenuName("후라이드")).willReturn(Optional.of(friedChicken));
        given(menuRepository.findByMenuName("양념")).willReturn(Optional.of(saucedChicken));

        List<Cart> result = orderService.returnToCartList(requestDto,testUser);

        /**
         * 1.요청 갯수만큼 카트에 잘 들어갔는지,
         * 2.카트에 메뉴,유저,가격 잘 들어갔는지
         */
        log.info("1번 카트 수량 : {}", result.get(1).getQuantity());
        log.info("1번 카트 총합 가격 : {}",result.get(1).getAmount());
        log.info("1번 카트 메뉴 이름 : {}", result.get(1).getMenu().getManuName());
        log.info("1번 카트 유저 이름 : {}", result.get(1).getUser().getUsername());

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(1).getMenu().getManuName()).isEqualTo("양념");
        assertThat(result.get(1).getAmount()).isEqualTo(saucedChicken.getAmount()*2);
    }

    @Test
    void 주문_생성_테스트() {
        /**
         * currentUser(완), foundStore(완), cartList(완),
         */

        OrderRequestDto requestDto = new OrderRequestDto();
        List<MenuItemDto> menuItems = new ArrayList<>();
        MenuItemDto fried = new MenuItemDto("후라이드",1);
        menuItems.add(fried);
        MenuItemDto sauced = new MenuItemDto("양념", 2);
        menuItems.add(sauced);
        ReflectionTestUtils.setField(requestDto,"menuItems",menuItems);
        given(menuRepository.findByMenuName("후라이드")).willReturn(Optional.of(friedChicken));
        given(menuRepository.findByMenuName("양념")).willReturn(Optional.of(saucedChicken));

        List<Cart> cartList = orderService.returnToCartList(requestDto,testUser);

        given(storeRepository.findById(1L)).willReturn(Optional.of(store));
        given(orderService.returnToCartList(requestDto,testUser)).willReturn(cartList);

        OrderResponseDto response = orderService.create(testUser,1L,requestDto);

        assertThat(response.getTotalAmount()).isEqualTo(36000+15000);
        assertThat(response.getStatus()).isEqualTo(OrderStatus.WAITING);
        assertThat(response.getUserId()).isEqualTo(1L);
    }
}
