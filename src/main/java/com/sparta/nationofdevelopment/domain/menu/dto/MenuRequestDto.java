package com.sparta.nationofdevelopment.domain.menu.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MenuRequestDto {
    @NotBlank
    private String menuName;

    @NotBlank
    private int amount;

    @NotBlank
    private String category;

    public MenuRequestDto(String menuName, int amount, String category) {
        this.menuName = menuName;
        this.amount = amount;
        this.category = category;
    }
}
