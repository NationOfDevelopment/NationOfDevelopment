package com.sparta.nationofdevelopment.common_entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {

    private final String message;
    private final Integer statusCode;
    private final T data;

    public static <T> ApiResponse<T> createSuccess(String message,Integer statusCode,T data) {

        return new ApiResponse<>(message,statusCode,data);

    }
    public static <T> ApiResponse<T> createError(String message,Integer statusCode) {
        return new ApiResponse<>(message,statusCode,null);
    }
}

