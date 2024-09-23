package com.sparta.nationofdevelopment.domain.user.dto.request;

import lombok.Getter;

import java.util.Date;
@Getter
public class UserBirthdayUpdateRequestDto {
    private Date birthday;

    public boolean isBirthdayValid() {
        //생일은 오늘보다 무조건 전이여야합니다.
        Date now = new Date();
        return birthday.before(now);
    }
}
