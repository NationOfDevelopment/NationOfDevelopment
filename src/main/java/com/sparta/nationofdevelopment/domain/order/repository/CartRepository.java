package com.sparta.nationofdevelopment.domain.order.repository;

import com.sparta.nationofdevelopment.domain.order.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByOrderId(long orderId);
}
