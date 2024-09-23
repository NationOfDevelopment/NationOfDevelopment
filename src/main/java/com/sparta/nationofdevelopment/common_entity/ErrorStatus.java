package com.sparta.nationofdevelopment.common_entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseCode{
    //jwt token 예외
    _BAD_REQUEST_UNSUPPORTED_TOKEN(HttpStatus.BAD_REQUEST,400,"지원되지 않는 JWT 토큰입니다."),
    _BAD_REQUEST_ILLEGAL_TOKEN(HttpStatus.BAD_REQUEST,400,"잘못된 JWT 토큰입니다."),
    _UNAUTHORIZED_INVALID_TOKEN(HttpStatus.UNAUTHORIZED,401,"유효하지 않는 JWT 서명입니다."),
    _UNAUTHORIZED_EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED,401,"만료된 JWT 토큰입니다."),
    _UNAUTHORIZED_TOKEN(HttpStatus.UNAUTHORIZED,401,"JWT 토큰 검증 중 오류가 발생했습니다."),
    _FORBIDDEN_TOKEN(HttpStatus.FORBIDDEN, 403, "관리자 권한이 없습니다."),
    _NOT_FOUND_TOKEN(HttpStatus.NOT_FOUND, 404, "JWT 토큰이 필요합니다."),

    _TEST_ERROR(HttpStatus.NO_CONTENT, 404, "ApiException 예외 처리 테스트"),

    // 메뉴 예외
    _AUTH_ADMIN_MENU(HttpStatus.FORBIDDEN, 403, "메뉴 생성 및 수정은 사장님만 가능합니다."),
    _NOT_FOUND_MENU(HttpStatus.NOT_FOUND, 404, "해당 메뉴를 찾을 수 없습니다."),

    // 가게 예외
    _NOT_FOUND_STORE(HttpStatus.NOT_FOUND, 404, "가게를 찾을 수 없습니다.");


    private final HttpStatus httpStatus;
    private final Integer statusCode;
    private final String message;

    @Override
    public ReasonDto getReasonHttpStatus() {
        return ReasonDto.builder()
                .statusCode(statusCode)
                .httpStatus(httpStatus)
                .message(message)
                .build();
    }
}
