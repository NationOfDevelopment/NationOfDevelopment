package com.sparta.nationofdevelopment.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.Date;
import java.util.regex.Pattern;

@Getter
//코드상 @Setter가 필요없는것처럼 보여 삭제했습니다. -multiverse22
public class SignupRequestDto {
    @NotBlank
    private String email;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String userRole;
    @NotBlank
    private Date birthday;

    // 이메일 형식 유효성 검사
    public boolean isEmailValid() {
        /*
        ^:다음의 문자열로 시작함을 의미합니다
        A-Za-z0-9 : 영문자 대문자와 소문자와 숫자를 의미합니다.
        +_.- : 해당특수문자를 허용합니다
        + : 앞에있는문자가 하나이상반복될 수 있음을 의미합니다.

        @ :이메일주소에서 사용되는 @를 의미합니다.
        A-Za-z0-9.-: 영어 대문자와 영어소문자 그리고숫자와 . 그리고 -를 허용합니다
        $:다음의 문자열이 끝났음을 의미합니다.

        예시 multiverse22@gmail.com (O)
        FRIDAY_14@daum.net (O)
        friday!!@naver.com (X) !는 허용되지않은 특수문자.
        */

        //String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        //String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9]+.[A-Za-z0-9]$";
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        /*
        보다 명확한 이메일 정규식으로 변경했습니다.

        \\.[A-Za-z]{2,}->

        \\. : .으로 시작합니다
        [A-Za-z] : 영문자 대문자와 소문자를 의미합니다.
        {2,} : 맨뒤 즉 .com 과같이 최상위 도메인이 2글자 이상으로 끝나야만합니다.
        이런식으로 변경하면 그전에는 통과되던
        user@.com 로컬이 없이 도메인만 존재
        user@com 점없이 TLD(최상위 도메인 예) .org .com)만 존재하는경우
        user@email.c TLD가 1글자만 존재하는 경우
        와 같은 경우들도 통과하지않습니다.
        */

        return Pattern.matches(emailRegex, this.email);
    }

    // 비밀번호 형식 유효성 검사
    public boolean isPasswordValid() {
        // 최소 8자, 대소문자, 숫자, 특수문자 각각 최소 1개 포함
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return Pattern.matches(passwordRegex, this.password);
    }
}

