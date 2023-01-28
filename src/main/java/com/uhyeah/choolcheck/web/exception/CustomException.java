package com.uhyeah.choolcheck.web.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException{

    private final StatusCode statusCode;

    public CustomException(StatusCode statusCode, String value) {
        super(value);
        this.statusCode = statusCode;
    }


}
