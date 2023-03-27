package com.uhyeah.choolcheck.global.exception;

import lombok.Builder;
import lombok.Getter;
@Getter
public class CustomException extends RuntimeException {

    private final StatusCode statusCode;

    private String message;

    private String fieldName;

    private String rejectValue;

    @Builder
    public CustomException(StatusCode statusCode, String message, String fieldName, String rejectValue) {
        this.statusCode = statusCode;
        this.message = message;
        this.fieldName = fieldName;
        this.rejectValue = rejectValue;
    }
}
