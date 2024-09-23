package com.sparta.nationofdevelopment.domain.user.controller;

import com.sparta.nationofdevelopment.common_entity.ApiResponse;
import com.sparta.nationofdevelopment.domain.common.annotation.Auth;
import com.sparta.nationofdevelopment.domain.common.dto.AuthUser;
import com.sparta.nationofdevelopment.domain.user.dto.request.UserGetRequestDto;
import com.sparta.nationofdevelopment.domain.user.dto.response.UserGetResponseDto;
import com.sparta.nationofdevelopment.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

//   @PutMapping
//   public ApiResponse<UserInfoUpdateResponseDto>

}
