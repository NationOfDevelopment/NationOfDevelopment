package com.sparta.nationofdevelopment.authtest;

import com.sparta.nationofdevelopment.config.JwtUtil;
import com.sparta.nationofdevelopment.config.PasswordEncoder;
import com.sparta.nationofdevelopment.domain.auth.dto.request.LoginRequestDto;
import com.sparta.nationofdevelopment.domain.auth.dto.request.SignupRequestDto;
import com.sparta.nationofdevelopment.domain.auth.dto.response.LoginResponseDto;
import com.sparta.nationofdevelopment.domain.auth.service.AuthService;
import com.sparta.nationofdevelopment.domain.common.exception.InvalidRequestException;
import com.sparta.nationofdevelopment.domain.user.entity.Users;
import com.sparta.nationofdevelopment.domain.user.enums.UserRole;
import com.sparta.nationofdevelopment.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AuthTest {
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtUtil jwtUtil;
    @InjectMocks
    private AuthService authService;

    @Test
    public void 회원가입시_email이_올바르지않으면() {
        Date today = new Date();

        SignupRequestDto requestDto = new SignupRequestDto(
                "asd",
                "testusername",
                "Multiverse22@",
                "user",
                today
        );
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.signup(requestDto);
        });
        assertEquals(exception.getMessage(), "이메일 형식이 올바르지 않습니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void 회원가입시_이메일이_비었거나_null이면(String email) {
        Date today = new Date();

        SignupRequestDto requestDto = new SignupRequestDto(
                email,
                "testusername",
                "testpassword",
                "user",
                today
        );
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.signup(requestDto);
        });
        assertEquals(exception.getMessage(), "이름은 공백 또는 null일 수 없습니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void 회원가입시_비밀번호가_비었거나_null이면(String password) {
        Date today = new Date();

        SignupRequestDto requestDto = new SignupRequestDto(
                "asd@gmail.com",
                "testusername",
                password,
                "user",
                today
        );
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.signup(requestDto);
        });
        assertEquals(exception.getMessage(), "비밀번호는 공백 또는 null일 수 없습니다.");
    }

    @Test
    public void 회원가입시_비밀번호가_유효하지않으면() {
        Date today = new Date();

        SignupRequestDto requestDto = new SignupRequestDto(
                "asd@gmail.com",
                "testusername",
                "111",
                "user",
                today
        );
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.signup(requestDto);
        });
        assertEquals(exception.getMessage(), "비밀번호는 최소 8자 이상이어야 하며, 대소문자 포함 영문, 숫자, 특수문자를 최소 1글자씩 포함해야 합니다.");
    }

    @Test
    public void 회원가입시_UserRole이_null이라면() {
        Date today = new Date();

        SignupRequestDto requestDto = new SignupRequestDto(
                "asd@gmail.com",
                "testusername",
                "Multiverse22@",
                "",
                today
        );
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
            authService.signup(requestDto);
        });
        assertEquals(exception.getMessage(), "유효하지 않은 UserRole");
    }

    @Test
    public void 회원가입시_중복이메일이_존재한다면() {
        Date today = new Date();

        SignupRequestDto requestDto = new SignupRequestDto(
                "asd@gmail.com",
                "testusername",
                "Multiverse22@",
                "user",
                today
        );
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(new Users()));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.signup(requestDto);
        });
        assertEquals(exception.getMessage(), "중복된 Email 입니다.");
    }

    @Test
    public void 회원가입_user_save_성공() {
        Date today = new Date();

        SignupRequestDto requestDto = new SignupRequestDto(
                "asd@gmail.com",
                "testusername",
                "Multiverse22@",
                "user",
                today
        );
        Users user = new Users(
                "asd@gmail.com",
                "testusername",
                "Multiverse22@",
                today,
                UserRole.USER
                );
        given(userRepository.findByEmail("asd@gmail.com")).willReturn(Optional.empty());
        given(userRepository.save(any(Users.class))).willReturn(user);

        authService.signup(requestDto);
        Users saveduser = userRepository.save(user);

        assertEquals("asd@gmail.com",saveduser.getEmail());
        assertEquals("testusername",saveduser.getUsername());
    }
    @Test
    public void login_정상작동() {
        Date today = new Date();
        Long userId = 1L;
        String rawpassword = "Multiverse22@";
        Users user = new Users(
                "asd@gmail.com",
                "testusername",
                "$2a$04$CuceZuvvKqxP8AHhqUYl5.KtyW4Yuek3MUR3Av33c7zjSVfPM9iyy",
                today,
                UserRole.USER
        );

        LoginRequestDto requestDto = new LoginRequestDto("asd@gmail.com", rawpassword);
        ReflectionTestUtils.setField(user,"id",userId);

        given(userRepository.findByEmail("asd@gmail.com")).willReturn(Optional.of(user));
        given(passwordEncoder.matches(rawpassword,"$2a$04$CuceZuvvKqxP8AHhqUYl5.KtyW4Yuek3MUR3Av33c7zjSVfPM9iyy")).
                willReturn(true);
        given(jwtUtil.createToken(userId,user.getEmail(),user.getUsername(),user.getUserRole())).willReturn(anyString());
        LoginResponseDto response = authService.login(requestDto);

        String bearerToken = response.getBearerToken();

        assertNotNull(bearerToken);
    }

}
