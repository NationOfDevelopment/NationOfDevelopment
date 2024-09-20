package com.sparta.nationofdevelopment.domain.menu.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MenuStatus {
    SALE("판매중"),
    DETAILED("삭제");

    private final String description;
}
