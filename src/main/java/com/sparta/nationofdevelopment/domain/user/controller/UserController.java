package com.sparta.nationofdevelopment.domain.user.controller;

import com.sparta.nationofdevelopment.common_entity.ApiResponse;
import com.sparta.nationofdevelopment.domain.common.annotation.Auth;
import com.sparta.nationofdevelopment.domain.common.dto.AuthUser;
import com.sparta.nationofdevelopment.domain.user.dto.request.UserBirthdayUpdateRequestDto;
import com.sparta.nationofdevelopment.domain.user.dto.request.UserDeleteRequestDto;
import com.sparta.nationofdevelopment.domain.user.dto.request.UserInfoUpdateRequestDto;
import com.sparta.nationofdevelopment.domain.user.dto.request.UserPasswordUpdateRequestDto;
import com.sparta.nationofdevelopment.domain.user.dto.response.UserGetResponseDto;
import com.sparta.nationofdevelopment.domain.user.dto.response.UserNameUpdateResponseDto;
import com.sparta.nationofdevelopment.domain.user.service.UserService;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @GetMapping("/users")
    public ApiResponse<UserGetResponseDto> getUserInfo(
            @Auth AuthUser authUser) {
        UserGetResponseDto responseDto = userService.getUserInfo(authUser);
        return ApiResponse.onSuccess(responseDto);
    }
    @PutMapping("/users")
    public ApiResponse<Null> updateUserInfo(
            @Auth AuthUser authUser,
            @RequestBody UserInfoUpdateRequestDto userInfoUpdateRequestDto
    ) {
        userService.updateUserInfo(authUser, userInfoUpdateRequestDto);
        return ApiResponse.onSuccess(null);
    }
//    @PutMapping("/users/change-username")
//    public ApiResponse<Null> updateUserName(
//           @Auth AuthUser authUser,
//           @RequestBody UserInfoUpdateRequestDto requestDto) {
//        userService.updateUserName(authUser,requestDto);
//        return ApiResponse.onSuccess(null);
//    }
//    @PutMapping("/users/change-birthday")
//    public ApiResponse<Null> updateUserBirthday(
//            @Auth AuthUser authUser,
//            @RequestBody UserBirthdayUpdateRequestDto requestDto) {
//        userService.updateBirthday(authUser,requestDto);
//        return ApiResponse.onSuccess(null);
//    }
    @PutMapping("/users/change-password")
    public ApiResponse<Null> updateUserPassword(
            @Auth AuthUser authUser,
            @RequestBody UserPasswordUpdateRequestDto requestDto) {
        userService.updatePassword(authUser,requestDto);
        return ApiResponse.onSuccess(null);
    }

    @DeleteMapping("/users")
    public ApiResponse<Null> deleteUsers(
            @Auth AuthUser authUser,
            @RequestBody UserDeleteRequestDto requestDto) {
        userService.deleteUser(authUser,requestDto);
        return ApiResponse.onSuccess(null);
    }
}
