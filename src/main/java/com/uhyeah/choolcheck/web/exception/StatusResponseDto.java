package com.uhyeah.choolcheck.web.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
public class StatusResponseDto {

    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String value;

    public StatusResponseDto(StatusCode statusCode, String value) {
        this.message = statusCode.getMessage();
        this.value = value;
    }

    @Override
    public String toString() {
        return "ExceptionResponseDto{" +
                ", message='" + message + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
