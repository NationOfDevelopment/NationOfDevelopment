package com.sparta.nationofdevelopment.domain.auth.service;

import com.sparta.nationofdevelopment.config.JwtUtil;
import com.sparta.nationofdevelopment.config.PasswordEncoder;
import com.sparta.nationofdevelopment.domain.auth.dto.request.LoginRequestDto;
import com.sparta.nationofdevelopment.domain.auth.dto.request.SignupRequestDto;
import com.sparta.nationofdevelopment.domain.auth.dto.response.LoginResponseDto;
import com.sparta.nationofdevelopment.domain.auth.dto.response.SignupResponseDto;
import com.sparta.nationofdevelopment.domain.auth.exception.AuthException;
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
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }

        // 비밀번호 형식 확인
        if (!requestDto.isPasswordValid()) {
            throw new IllegalArgumentException("비밀번호는 최소 8자 이상이어야 하며, 대소문자 포함 영문, 숫자, 특수문자를 최소 1글자씩 포함해야 합니다.");
        }

        String password = passwordEncoder.encode(requestDto.getPassword());

        // email 중복확인, email: unique=true
        Optional<Users> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }

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
        Users user = userRepository.findByEmailAndIsDeleted(loginRequestDto.getEmail(),false)
                .orElseThrow(() -> new AuthException("가입되지 않은 이메일입니다."));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new AuthException("잘못된 비밀번호입니다.");
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
