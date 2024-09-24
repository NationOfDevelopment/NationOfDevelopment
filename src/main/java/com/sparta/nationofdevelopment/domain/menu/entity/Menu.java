package com.sparta.nationofdevelopment.domain.menu.entity;

import com.sparta.nationofdevelopment.domain.menu.dto.MenuRequestDto;
import com.sparta.nationofdevelopment.domain.menu.enums.MenuStatus;
import com.sparta.nationofdevelopment.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Menu {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String menuName;
    private int amount;

    @Enumerated(EnumType.STRING)
    private MenuStatus state;

    private String category;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    public Menu(MenuRequestDto requestDto, Store store) {
        this.menuName = requestDto.getMenuName();
        this.amount = requestDto.getAmount();
        this.category = requestDto.getCategory();
        this.state = MenuStatus.SALE;
        this.store = store;
    }

    public void update(MenuRequestDto requestDto) {
        this.menuName = requestDto.getMenuName();
        this.amount = requestDto.getAmount();
        this.category = requestDto.getCategory();
    }

    public void delete() {
        this.state = MenuStatus.DELETED;
    }
}
