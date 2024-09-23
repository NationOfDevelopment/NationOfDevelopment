package com.sparta.nationofdevelopment.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserPasswordUpdateRequestDto {
    private String oldPassword;
    private String newPassword;
}
