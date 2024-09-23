package com.sparta.nationofdevelopment.domain.user.enums;

import com.sparta.nationofdevelopment.domain.common.exception.InvalidRequestException;

import java.util.Arrays;

public enum UserRole {
    OWNER, USER,ADMIN;
    //일반USER,사장님OWNER,개발자ADMIN

    public static UserRole of(String role) {
        return Arrays.stream(UserRole.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new InvalidRequestException("유효하지 않은 UserRole"));
    }
}
