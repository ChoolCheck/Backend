package com.uhyeah.choolcheck.web.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleValidException(MethodArgumentNotValidException e) {

        log.error("[exceptionHandle] MethodArgumentNotValidException");
        StatusCode errorCode = StatusCode.INVALID_PARAMETER;
        BindingResult bindingResult = e.getBindingResult();

        StringBuilder stringBuilder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            stringBuilder.append("[");
            stringBuilder.append(fieldError.getDefaultMessage());
            stringBuilder.append("]");
        }

        return new ResponseEntity(errorCode.getMessage() + " > " + stringBuilder.toString(), errorCode.getHttpStatus());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgumentException(IllegalArgumentException e) {

        log.error("[exceptionHandle] IllegalArgumentException");
        StatusCode errorCode = StatusCode.RESOURCE_NOT_FOUND;

        return new ResponseEntity(errorCode.getMessage() + ">" + e.getMessage(), errorCode.getHttpStatus());
    }

}
