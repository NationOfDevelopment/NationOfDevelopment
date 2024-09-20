package com.sparta.nationofdevelopment.domain.store.repository;

import com.sparta.nationofdevelopment.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
