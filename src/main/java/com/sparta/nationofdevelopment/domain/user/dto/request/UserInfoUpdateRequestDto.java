package com.sparta.nationofdevelopment.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class UserInfoUpdateRequestDto {
    private String newUserName;
    private Date newUserBirthday;

    public UserInfoUpdateRequestDto(String newUserName, Date newUserBirthday) {
        this.newUserName = newUserName;
        this.newUserBirthday = newUserBirthday;
    }
}
