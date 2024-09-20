package com.sparta.nationofdevelopment.domain.common.exception;

import com.sparta.nationofdevelopment.common_entity.BaseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiException extends RuntimeException {
    private final BaseCode errorCode;
}
