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


    //주문 관련 예외
    _BAD_REQUEST_NOT_FOUND_ORDER(HttpStatus.BAD_REQUEST, 400, "해당 주문을 찾을 수 없습니다.."),
    _BAD_REQUEST_INVALID_STATUS_ACCEPTED_OR_REJECTED(HttpStatus.BAD_REQUEST,400, "수락 대기 상태의 주문만 수락하거나 거절할 수 있습니다."),
    _BAD_REQUEST_INVALID_STATUS_INVALID_ORDER(HttpStatus.BAD_REQUEST,400, "진행 상태의 주문만 완료하거나 취소할 수 있습니다."),
    _FORBIDDEN_NO_AUTHORITY_MANAGE_ORDER(HttpStatus.FORBIDDEN,403,"해당 가게 사장님만 해당 주문을 관리할 수 있습니다."),


    _TEST_ERROR(HttpStatus.NO_CONTENT, 404, "ApiException 예외 처리 테스트");

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
