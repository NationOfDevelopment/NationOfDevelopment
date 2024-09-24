package com.sparta.nationofdevelopment.domain.order.repository;

import com.sparta.nationofdevelopment.domain.order.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByOrderId(long orderId);

    @Query("SELECT c FROM Cart c WHERE c.orderId IS NULL AND c.createdAt < :timeLimit")
    List<Cart> findInvalidCarts(LocalDateTime timeLimit);

    @Query("SELECT c FROM Cart c WHERE c.orderId IN :orderIds")
    List<Cart> findCartsByOrderIds(@Param("orderIds") List<Long>orderIds);

    @Query("SELECT c FROM Cart c WHERE c.user.id = :userId AND c.menu.id = :menuId AND c.orderId = 0")
    Optional<Cart> findByUserIdAndMenuIdAndOrderIdIsZero(@Param("userId") Long userId, @Param("menuId") Long menuId);

    //주문이 확정되지 않은 장바구니 리스트를 불러오기
    @Query("SELECT c FROM Cart c WHERE c.user.id = :userId AND c.orderId = 0")
    List<Cart> findByUserIdAndOrderIdIsZero(@Param("userId") Long userId);

    //주문이 확정된 장바구니는 조회하지 않기
    @Query("SELECT c FROM Cart c WHERE c.user.id = :userId AND c.orderId != 0")
    List<Cart> findByUserIdAndOrderIdNotZero(@Param("userId") Long userId);


}

