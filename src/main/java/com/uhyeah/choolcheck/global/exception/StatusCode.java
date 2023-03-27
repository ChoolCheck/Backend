package com.uhyeah.choolcheck.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum StatusCode {

    //ERROR
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND),
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED);

    private final HttpStatus httpStatus;

}
