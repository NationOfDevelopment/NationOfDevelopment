//package com.sparta.nationofdevelopment.domain.order.service;
//
//import com.sparta.nationofdevelopment.domain.common.dto.AuthUser;
//import com.sparta.nationofdevelopment.domain.common.exception.ApiException;
//import com.sparta.nationofdevelopment.domain.menu.entity.Menu;
//import com.sparta.nationofdevelopment.domain.menu.repository.MenuRepository;
//import com.sparta.nationofdevelopment.domain.order.OrderStatus;
//import com.sparta.nationofdevelopment.domain.order.dto.MenuItemDto;
//import com.sparta.nationofdevelopment.domain.order.dto.OrderRequestDto;
//import com.sparta.nationofdevelopment.domain.order.dto.OrderResponseDto;
//import com.sparta.nationofdevelopment.domain.order.dto.OrderStatusRequestDto;
//import com.sparta.nationofdevelopment.domain.order.entity.Cart;
//import com.sparta.nationofdevelopment.domain.order.entity.Orders;
//import com.sparta.nationofdevelopment.domain.order.repository.CartRepository;
//import com.sparta.nationofdevelopment.domain.order.repository.OrderRepository;
//import com.sparta.nationofdevelopment.domain.store.entity.Store;
//import com.sparta.nationofdevelopment.domain.store.repository.StoreRepository;
//import com.sparta.nationofdevelopment.domain.user.entity.Users;
//import com.sparta.nationofdevelopment.domain.user.enums.UserRole;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Spy;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.BDDMockito.given;
//
//@Slf4j
//@ExtendWith(MockitoExtension.class)
//class OrderServiceTest {
//    @Mock
//    CartRepository cartRepository;
//    @Mock
//    OrderRepository orderRepository;
//    @Mock
//    StoreRepository storeRepository;
//    @Mock
//    MenuRepository menuRepository;
//    @Spy
//    @InjectMocks
//    OrderService orderService;
//
//    /**
//     * convertToCartList(임시)
//     * requestDto : menuItemDto 리스트
//     * menuItemDto : String menuName, int quantity
//     * AuthUser 필요,
//     * 메뉴,스토어,유저 있다고 치고
//     */
//    private AuthUser testUser;
//    private Menu friedChicken;
//    private Menu saucedChicken;
//    private Store store;
//
//    @BeforeEach
//    void setUp() {
//        testUser = new AuthUser(1L,"tester@email.com","테스터1", UserRole.USER);
//        friedChicken = new Menu();
//        friedChicken.setId(1L);
//        friedChicken.setMenuName("후라이드");
//        friedChicken.setAmount(15000);
//
//        saucedChicken = new Menu();
//        saucedChicken.setId(2L);
//        saucedChicken.setMenuName("양념");
//        saucedChicken.setAmount(18000);
//
//        store = new Store();
//        store.setStoreId(1L);
//
//    }
//
//    @Test
//    void 장바구니_생성_테스트() {
//        OrderRequestDto requestDto = new OrderRequestDto();
//        List<MenuItemDto> menuItems = new ArrayList<>();
//        MenuItemDto fried = new MenuItemDto("후라이드",1);
//        menuItems.add(fried);
//        MenuItemDto sauced = new MenuItemDto("양념", 2);
//        menuItems.add(sauced);
//        ReflectionTestUtils.setField(requestDto,"menuItems",menuItems);
//        given(menuRepository.findByMenuName("후라이드")).willReturn(Optional.of(friedChicken));
//        given(menuRepository.findByMenuName("양념")).willReturn(Optional.of(saucedChicken));
//
//        List<Cart> result = orderService.convertToCartList(requestDto,testUser);
//
//        /*
//         * 1.요청 갯수만큼 카트에 잘 들어갔는지,
//         * 2.카트에 메뉴,유저,가격 잘 들어갔는지
//         */
//        log.info("1번 카트 수량 : {}", result.get(1).getQuantity());
//        log.info("1번 카트 총합 가격 : {}",result.get(1).getAmount());
//        log.info("1번 카트 메뉴 이름 : {}", result.get(1).getMenu().getMenuName());
//        log.info("1번 카트 유저 이름 : {}", result.get(1).getUser().getUsername());
//
//        assertThat(result).hasSize(2);
//        assertThat(result.get(1).getMenu().getMenuName()).isEqualTo("양념");
//        assertThat(result.get(1).getAmount()).isEqualTo(saucedChicken.getAmount()*2);
//    }
//
//    @Test
//    void 주문_생성_테스트() {
//        OrderRequestDto requestDto = new OrderRequestDto();
//        List<MenuItemDto> menuItems = new ArrayList<>();
//        MenuItemDto fried = new MenuItemDto("후라이드",1);
//        menuItems.add(fried);
//        MenuItemDto sauced = new MenuItemDto("양념", 2);
//        menuItems.add(sauced);
//        ReflectionTestUtils.setField(requestDto,"menuItems",menuItems);
//        given(menuRepository.findByMenuName("후라이드")).willReturn(Optional.of(friedChicken));
//        given(menuRepository.findByMenuName("양념")).willReturn(Optional.of(saucedChicken));
//
//        List<Cart> cartList = orderService.convertToCartList(requestDto,testUser);
//
//        given(storeRepository.findById(1L)).willReturn(Optional.of(store));
//        given(orderService.convertToCartList(requestDto,testUser)).willReturn(cartList);
//
//        OrderResponseDto response = orderService.create(testUser,1L,requestDto);
//
//        assertThat(response.getTotalAmount()).isEqualTo(36000+15000);
//        assertThat(response.getStatus()).isEqualTo(OrderStatus.WAITING);
//        assertThat(response.getUserId()).isEqualTo(1L);
//    }
//
//    @Nested
//    class 주문_상태_변경{
//        @Test
//        void 수락_대기_아닌_주문에_배달완료_누를_경우() {
//            OrderStatusRequestDto requestDto = new OrderStatusRequestDto("ACCEPTED");
//            Orders order = new Orders(32000, Users.fromAuthUser(testUser),store,OrderStatus.DELIVERED);
//            Users currentUser = Users.fromAuthUser(testUser);
//            order.getStore().setUser(currentUser);
//
//            given(orderRepository.findById(1L)).willReturn(Optional.of(order));
//
//            ApiException e = assertThrows(ApiException.class, () -> orderService.changeStatus(testUser,1L,requestDto));
//            assertEquals("수락 대기 상태의 주문만 수락하거나 거절할 수 있습니다.", e.getErrorCode().getReasonHttpStatus().getMessage());
//        }
//    }
//}
