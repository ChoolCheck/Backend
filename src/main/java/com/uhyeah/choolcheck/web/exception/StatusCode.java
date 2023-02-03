package com.uhyeah.choolcheck.web.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum StatusCode {

    //ERROR
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "잘못된 파라미터 값입니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 값입니다."),
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "중복된 값 입니다."),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "비인증 사용자 입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
