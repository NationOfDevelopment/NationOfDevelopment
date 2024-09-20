package com.sparta.nationofdevelopment.domain.order.repository;

import com.sparta.nationofdevelopment.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
