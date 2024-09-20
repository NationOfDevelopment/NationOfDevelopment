package com.sparta.nationofdevelopment.domain.menu.entity;

import com.sparta.nationofdevelopment.domain.menu.dto.MenuRequestDto;
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
    private Long amount;

    @Enumerated(EnumType.STRING)
    private String state;

    private String category;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    public Menu(MenuRequestDto requestDto) {
        this.menuName = requestDto.getMenuName();
        this.amount = requestDto.getAmount();
        this.category = requestDto.getCategory();
    }
}
