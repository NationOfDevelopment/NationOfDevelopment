package com.sparta.nationofdevelopment.config;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.sparta.nationofdevelopment.common_entity.ErrorStatus;
import com.sparta.nationofdevelopment.domain.common.exception.ApiException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class PasswordEncoder {

    private static final String PASSWORD_REGEX="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    public boolean isPasswordValid(String password) {
        if(Strings.isBlank(password)) {
            throw new ApiException(ErrorStatus._INVALID_PASSWORD_FORM);
        }
        // 최소 8자, 대소문자, 숫자, 특수문자 각각 최소 1개 포함
        return Pattern.matches(PASSWORD_REGEX, password);
    }
    public String encode(String rawPassword) {
        return BCrypt.withDefaults().hashToString(BCrypt.MIN_COST, rawPassword.toCharArray());
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), encodedPassword);
        return result.verified;
    }
}