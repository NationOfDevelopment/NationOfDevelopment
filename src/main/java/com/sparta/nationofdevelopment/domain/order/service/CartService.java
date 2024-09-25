package com.sparta.nationofdevelopment.domain.order.service;

import com.sparta.nationofdevelopment.common_entity.ErrorStatus;
import com.sparta.nationofdevelopment.domain.common.dto.AuthUser;
import com.sparta.nationofdevelopment.domain.common.exception.ApiException;
import com.sparta.nationofdevelopment.domain.common.module.Finder;
import com.sparta.nationofdevelopment.domain.menu.entity.Menu;
import com.sparta.nationofdevelopment.domain.order.dto.requestDto.MenuItemDto;
import com.sparta.nationofdevelopment.domain.order.dto.responseDto.CartDto;
import com.sparta.nationofdevelopment.domain.order.entity.Cart;
import com.sparta.nationofdevelopment.domain.order.entity.Orders;
import com.sparta.nationofdevelopment.domain.order.repository.CartRepository;
import com.sparta.nationofdevelopment.domain.user.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final CartRepository cartRepository;
    private final Finder finder;

    //장바구니 추가
    public CartDto createCart(MenuItemDto request, AuthUser authUser, long storeId) {
        Users currentUser = Users.fromAuthUser(authUser);
        Menu foundmenu = finder.findMenuByMenuNameAndStoreId(request.getMenuName(),storeId);

        validateDuplicateMenu(currentUser,foundmenu);
        validateAndClearPreviousCart(currentUser,foundmenu);

        Cart savedCart = cartRepository.save(new Cart(request, authUser, foundmenu));
        return new CartDto(savedCart);
    }

    //같은 가게에서 같은 메뉴를 장바구니에 담는지 검증
    private void validateDuplicateMenu(Users currentUser, Menu foundMenu) {
        Optional<Cart> existingCart = cartRepository.findByUserIdAndMenuIdAndOrderIdIsZero(currentUser.getId(),foundMenu.getId());
        if(existingCart.isPresent() && existingCart.get().getOrderId() == 0L) {
            throw new ApiException(ErrorStatus._BAD_REQUEST_DUPLICATE_CART_ITEM);
        }
    }

    //이전 가게와 다른 가게의 메뉴를 장바구니에 넣는 경우 해당 장바구니 삭제
    private void validateAndClearPreviousCart(Users currentUser, Menu foundMenu) {
        List<Cart> usersPreviousCartList = cartRepository.findByUserIdAndOrderIdIsZero(currentUser.getId());
        if (!usersPreviousCartList.isEmpty() && !usersPreviousCartList.get(0).getMenu().getStore().getStoreId().equals(foundMenu.getStore().getStoreId())) {
            cartRepository.deleteAll(usersPreviousCartList);
        }
    }

    //장바구니 삭제
    public String deleteCart (AuthUser authUser, long cartId) {
        Users currentUser = Users.fromAuthUser(authUser);
        Cart foundCart = finder.findCartByCartId(cartId);

        validateDeletingCart(currentUser,foundCart);

        cartRepository.delete(foundCart);
        return "장바구니 삭제가 완료되었습니다.";
    }

    //장바구니 삭제 검증
    private void validateDeletingCart(Users currentUser, Cart foundCart) {
        //장바구니를 생성한 유저가 아닌 사람이 장바구니를 삭제하려고 하는 경우 예외처리
        if(!currentUser.getId().equals(foundCart.getUser().getId())) {
            throw new ApiException(ErrorStatus._FORBIDDEN_DELETE_CART);
        }
        //진행중인 주문으로 바뀐 장바구니를 삭제하려는 경우 예외처리
        if (foundCart.getOrderId() != 0L) {
            throw new ApiException(ErrorStatus._BAD_REQUEST_UNABLE_TO_DELETE_CART);  // 예외 처리
        }
    }

    //현재 해당 유저의 장바구니 목록 조회
    @Transactional(readOnly = true)
    public List<CartDto> findCart (AuthUser authUser) {
        Users currentUser = Users.fromAuthUser(authUser);
        return cartRepository.findByUserIdAndOrderIdIsZero(currentUser.getId()).stream().map(CartDto::new).toList();
    }

    //주문이 확정되었을 경우 장바구니에 주문Id 추가
    public void saveOrderIdsToCarts(List<Cart> cartList, Orders savedOrder) {
        for (Cart cart : cartList) {
            cart.setOrderId(savedOrder.getId());
        }
        cartRepository.saveAll(cartList);
    }
}
