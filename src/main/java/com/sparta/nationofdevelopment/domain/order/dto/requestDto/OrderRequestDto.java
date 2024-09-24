package com.sparta.nationofdevelopment.domain.order.dto.requestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderRequestDto {
    private List<MenuItemDto> menuItems;
}
