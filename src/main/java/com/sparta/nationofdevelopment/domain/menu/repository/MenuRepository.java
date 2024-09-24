package com.sparta.nationofdevelopment.domain.menu.repository;

import com.sparta.nationofdevelopment.domain.menu.entity.Menu;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    Optional<Menu> findByMenuName(@NotNull String menuName);

    Optional<Menu> findByMenuNameAndStore_storeId(String menuName, long storeId);
}
