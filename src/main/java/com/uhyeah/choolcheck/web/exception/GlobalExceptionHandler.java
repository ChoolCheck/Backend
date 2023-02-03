package com.uhyeah.choolcheck.web.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

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

        StatusResponseDto exceptionResponseDto = new StatusResponseDto(StatusCode.INVALID_PARAMETER, eMessage.toString());

        log.error("[exceptionHandle] MethodArgumentNotValidException" + exceptionResponseDto.toString());
        return new ResponseEntity(exceptionResponseDto, StatusCode.INVALID_PARAMETER.getHttpStatus());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity handleBadCredentialsException(BadCredentialsException e) {

        StatusResponseDto exceptionResponseDto = new StatusResponseDto(StatusCode.UNAUTHORIZED_USER, "비밀번호가 일치하지 않습니다.");

        log.error("[exceptionHandle] BadCredentialsException = {}", exceptionResponseDto.toString());
        return new ResponseEntity(exceptionResponseDto, StatusCode.UNAUTHORIZED_USER.getHttpStatus());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity handleUsernameNotFoundException(UsernameNotFoundException e) {

        StatusResponseDto exceptionResponseDto = new StatusResponseDto(StatusCode.UNAUTHORIZED_USER, "존재하지 않는 사용자 이메일입니다.");

        log.error("[exceptionHandle] BadCredentialsException = {}", exceptionResponseDto.toString());
        return new ResponseEntity(exceptionResponseDto, StatusCode.UNAUTHORIZED_USER.getHttpStatus());
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity handleInvalidFormatException(InvalidFormatException e) {

        StatusResponseDto exceptionResponseDto = new StatusResponseDto(StatusCode.INVALID_PARAMETER, e.getPathReference() + ": " + e.getValue().toString());

        log.error("[exceptionHandle] InvalidFOrmatException = {}", exceptionResponseDto.toString());
        return new ResponseEntity(exceptionResponseDto, StatusCode.UNAUTHORIZED_USER.getHttpStatus());
    }
}
