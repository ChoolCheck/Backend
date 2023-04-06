package com.uhyeah.choolcheck.global.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class StatusResponseDto {

    private final StatusCode statusCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String fieldName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String rejectValue;
}
