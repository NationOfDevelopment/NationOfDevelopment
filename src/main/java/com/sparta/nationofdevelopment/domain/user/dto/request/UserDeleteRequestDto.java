package com.sparta.nationofdevelopment.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserDeleteRequestDto {
    private String password;

    public UserDeleteRequestDto(String password) {
        this.password = password;
    }
}
