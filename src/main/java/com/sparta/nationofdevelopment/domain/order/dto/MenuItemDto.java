package com.sparta.nationofdevelopment.domain.order.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class MenuItemDto {

    @NotNull
    private String menuName;
    @Min(1)
    private int quantity;
}
