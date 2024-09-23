package com.sparta.nationofdevelopment.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.nationofdevelopment.domain.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserGetResponseDto {

    private String email;
    private String username;
    private Date birthday;
    private UserRole userRole;
    //사장님전용
    private Integer storeCount=null;
    private List<String> storeList=null;

    public UserGetResponseDto(String email, String username, Date birthday, UserRole userRole) {
        this.email = email;
        this.username = username;
        this.birthday = birthday;
        this.userRole = userRole;
    }
}
