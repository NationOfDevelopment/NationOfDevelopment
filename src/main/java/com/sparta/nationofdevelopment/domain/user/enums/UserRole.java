package com.sparta.nationofdevelopment.domain.user.enums;

import com.sparta.nationofdevelopment.common_entity.ErrorStatus;
import com.sparta.nationofdevelopment.domain.common.exception.ApiException;
import com.sparta.nationofdevelopment.domain.common.exception.InvalidRequestException;

import java.util.Arrays;

public enum UserRole {
    OWNER, USER,ADMIN;
    //일반USER,사장님OWNER,개발자ADMIN

    public static UserRole of(String role) {
        return Arrays.stream(UserRole.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new ApiException(ErrorStatus._INVALID_USER_ROLE));
    }
}
