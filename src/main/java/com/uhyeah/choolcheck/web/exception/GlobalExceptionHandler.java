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

    @ExceptionHandler(CustomException.class)
    public ResponseEntity handleCustomException(CustomException e) {

        StatusResponseDto exceptionResponseDto = new StatusResponseDto(e.getStatusCode(), e.getMessage());

        log.error("[exceptionHandle] CustomException " + exceptionResponseDto.toString());
        return new ResponseEntity(exceptionResponseDto, e.getStatusCode().getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleValidException(MethodArgumentNotValidException e) {

        BindingResult bindingResult = e.getBindingResult();

        StringBuilder eMessage = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            eMessage.append("[");
            eMessage.append(fieldError.getDefaultMessage());
            eMessage.append("]");
        }

        StatusResponseDto exceptionResponseDto = new StatusResponseDto(StatusCode.INVALID_PARAMETER, e.getMessage());

        log.error("[exceptionHandle] MethodArgumentNotValidException" + exceptionResponseDto.toString());
        return new ResponseEntity(exceptionResponseDto, StatusCode.INVALID_PARAMETER.getHttpStatus());
    }



}
