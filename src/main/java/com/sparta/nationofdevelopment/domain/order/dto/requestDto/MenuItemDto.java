package com.sparta.nationofdevelopment.domain.order.dto.requestDto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemDto {
    @NotNull
    private String menuName;
    @Min(1)
    private int quantity;
}
