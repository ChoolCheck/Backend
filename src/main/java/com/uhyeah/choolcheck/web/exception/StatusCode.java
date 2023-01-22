package com.uhyeah.choolcheck.web.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum StatusCode {

    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "잘못된 파라미터 값입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
