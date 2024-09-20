package com.sparta.nationofdevelopment.domain.common.dto;

import com.sparta.nationofdevelopment.domain.user.enums.UserRole;
import lombok.Getter;

import java.util.Date;

@Getter
public class AuthUser {

    private final Long id;
    private final String email;
    private final String username;
    private final UserRole userRole;

    public AuthUser(Long id, String email,String username, UserRole userRole) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.userRole = userRole;
    }
}
