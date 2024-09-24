package com.sparta.nationofdevelopment.domain.order.repository;

import com.sparta.nationofdevelopment.domain.order.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByOrderId(long orderId);

    @Query("SELECT c FROM Cart c WHERE c.orderId IS NULL AND c.createdAt < :timeLimit")
    List<Cart> findInvalidCarts(LocalDateTime timeLimit);
}
