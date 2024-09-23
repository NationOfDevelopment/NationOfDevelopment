package com.sparta.nationofdevelopment.domain.order.repository;

import com.sparta.nationofdevelopment.domain.order.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByStore_StoreId(long storeId);
}
