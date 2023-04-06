package com.uhyeah.choolcheck.global.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomException extends RuntimeException {

    private final StatusCode statusCode;

    private final String message;

    private final String fieldName;

    private final String rejectValue;
}
