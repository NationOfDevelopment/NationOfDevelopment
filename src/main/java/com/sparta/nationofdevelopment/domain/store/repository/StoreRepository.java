package com.sparta.nationofdevelopment.domain.store.repository;

import com.sparta.nationofdevelopment.domain.store.dto.response.StoreResponseDto;
import com.sparta.nationofdevelopment.domain.store.entity.Store;
import com.sparta.nationofdevelopment.domain.store.entity.StoreStatus;
import com.sparta.nationofdevelopment.domain.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    int countByUserAndStatus(Users users, StoreStatus status);
    List<Store> findByStoreName(String storeName);
    List<Store> findByStatus(StoreStatus status);
}
