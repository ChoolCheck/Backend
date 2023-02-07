package com.uhyeah.choolcheck.web.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
public class StatusResponseDto {

    private StatusCode statusCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fieldName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String rejectValue;

    @Builder
    public StatusResponseDto(StatusCode statusCode, String message, String fieldName, String rejectValue) {
        this.statusCode = statusCode;
        this.message = message;
        this.fieldName = fieldName;
        this.rejectValue = rejectValue;
    }

    @Override
    public String toString() {
        return "StatusResponseDto{" +
                "statusCode=" + statusCode +
                ", message='" + message + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", rejectValue='" + rejectValue + '\'' +
                '}';
    }
}
