package com.sparta.nationofdevelopment.domain.auth.controller;

import com.sparta.nationofdevelopment.common_entity.ApiResponse;
import com.sparta.nationofdevelopment.domain.auth.dto.request.LoginRequestDto;
import com.sparta.nationofdevelopment.domain.auth.dto.request.SignupRequestDto;
import com.sparta.nationofdevelopment.domain.auth.dto.response.LoginResponseDto;
import com.sparta.nationofdevelopment.domain.auth.dto.response.SignupResponseDto;
import com.sparta.nationofdevelopment.domain.auth.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public ApiResponse<SignupResponseDto> signup(@RequestBody SignupRequestDto signupRequest) {
        SignupResponseDto signupResponse = authService.signup(signupRequest);
        return ApiResponse.createSuccess("회원가입 완료", HttpStatus.CREATED.value(), signupResponse);
    }

    @PostMapping("/auth/login")
    public ApiResponse<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto responseDto = authService.login(loginRequestDto);
        return ApiResponse.createSuccess("로그인 완료", HttpStatus.OK.value(), responseDto);
    }
}
