package com.sparta.nationofdevelopment.domain.auth.service;

import com.sparta.nationofdevelopment.common_entity.ErrorStatus;
import com.sparta.nationofdevelopment.config.JwtUtil;
import com.sparta.nationofdevelopment.config.PasswordEncoder;
import com.sparta.nationofdevelopment.domain.auth.dto.request.LoginRequestDto;
import com.sparta.nationofdevelopment.domain.auth.dto.request.SignupRequestDto;
import com.sparta.nationofdevelopment.domain.auth.dto.response.LoginResponseDto;
import com.sparta.nationofdevelopment.domain.auth.dto.response.SignupResponseDto;
import com.sparta.nationofdevelopment.domain.common.exception.ApiException;
import com.sparta.nationofdevelopment.domain.user.entity.Users;
import com.sparta.nationofdevelopment.domain.user.enums.UserRole;
import com.sparta.nationofdevelopment.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public SignupResponseDto signup(SignupRequestDto requestDto) {
        String email = requestDto.getEmail();
        String username = requestDto.getUsername();
        UserRole userRole = UserRole.of(requestDto.getUserRole());
        Date birthday = requestDto.getBirthday();

        // 이메일 형식 확인
        if(!requestDto.isEmailValid()) {
            throw new ApiException(ErrorStatus._INVALID_EMAIL_FORM);
        }
        // 비밀번호 형식 확인
        if (!requestDto.isPasswordValid()) {
            throw new ApiException(ErrorStatus._INVALID_PASSWORD_FORM);
        }

        if (!requestDto.isBirthdayValid()) {
            throw new ApiException(ErrorStatus._INVALID_BIRTHDAY);
        }

        Optional<Users> checkEmail = userRepository.findByEmail(email);

        if (checkEmail.isPresent()) {
            throw new ApiException(ErrorStatus._DUPLICATED_EMAIL);
        }
        String password = passwordEncoder.encode(requestDto.getPassword());

        // email 중복확인, email: unique=true



        Users user = new Users(email,username, password,birthday,userRole);

        Users savedUser = userRepository.save(user);

        String bearerToken = jwtUtil.createToken(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getUsername(),
                savedUser.getUserRole()
        );

        return new SignupResponseDto(bearerToken);
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Users user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_EMAIL));

        if (user.getIsDeleted()) {
            throw new ApiException(ErrorStatus._DELETED_USER);
        }
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new ApiException(ErrorStatus._PASSWORD_NOT_MATCHES);
        }

        String bearerToken = jwtUtil.createToken(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getUserRole()
        );

        return new LoginResponseDto(bearerToken);
    }
}
