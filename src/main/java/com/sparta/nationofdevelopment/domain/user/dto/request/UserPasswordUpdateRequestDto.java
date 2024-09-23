package com.sparta.nationofdevelopment.domain.user.dto.request;

import lombok.Getter;

@Getter
public class UserPasswordUpdateRequestDto {
    private String oldPassword;
    private String newPassword;
}
