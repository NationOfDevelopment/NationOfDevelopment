package com.sparta.nationofdevelopment.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class UserInfoUpdateRequestDto {
    private String newUserName;
}
