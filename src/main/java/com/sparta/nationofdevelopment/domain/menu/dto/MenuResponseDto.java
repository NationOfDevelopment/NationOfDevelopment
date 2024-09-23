package com.sparta.nationofdevelopment.domain.menu.dto;

import lombok.Getter;

@Getter
public class MenuResponseDto {

    private final Long id;
    private final String menuName;
    private final int amount;
    private final String category;

    public MenuResponseDto(Long id, String menuName, int amount, String category) {
        this.id = id;
        this.menuName = menuName;
        this.amount = amount;
        this.category = category;
    }
}
