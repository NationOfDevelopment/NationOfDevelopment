package com.sparta.nationofdevelopment.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class LoginResponseDto {
    private final String bearerToken;


    public LoginResponseDto(String bearerToken) {
        this.bearerToken = bearerToken;
    }
}
