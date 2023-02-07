package com.uhyeah.choolcheck.web.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity handleCustomException(CustomException e) {

        StatusResponseDto exceptionResponseDto = StatusResponseDto.builder()
                .statusCode(e.getStatusCode())
                .message(e.getMessage())
                .fieldName(e.getFieldName())
                .rejectValue(e.getRejectValue())
                .build();

        log.error("[exceptionHandle] CustomException " + exceptionResponseDto.toString());
        return new ResponseEntity(exceptionResponseDto, e.getStatusCode().getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleValidException(MethodArgumentNotValidException e) {

        StatusCode statusCode = StatusCode.INVALID_PARAMETER;
        BindingResult bindingResult = e.getBindingResult();

        List<StatusResponseDto> statusResponseDtoList = new ArrayList<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            statusResponseDtoList.add(StatusResponseDto.builder()
                    .statusCode(statusCode)
                    .message(fieldError.getDefaultMessage())
                    .fieldName(fieldError.getField())
                    .rejectValue(fieldError.getRejectedValue().toString())
                    .build());
        }

        log.error("[exceptionHandle] MethodArgumentNotValidException" + statusResponseDtoList.toString());
        return new ResponseEntity(statusResponseDtoList, statusCode.getHttpStatus());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity handleBadCredentialsException(BadCredentialsException e) {

        StatusCode statusCode = StatusCode.UNAUTHORIZED_USER;
        StatusResponseDto exceptionResponseDto = StatusResponseDto.builder()
                .statusCode(statusCode)
                .message("비밀번호가 일치하지 않습니다.")
                .fieldName("password")
                .build();

        log.error("[exceptionHandle] BadCredentialsException = {}", exceptionResponseDto.toString());
        return new ResponseEntity(exceptionResponseDto, statusCode.getHttpStatus());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity handleUsernameNotFoundException(UsernameNotFoundException e) {

        StatusCode statusCode = StatusCode.UNAUTHORIZED_USER;
        StatusResponseDto exceptionResponseDto = StatusResponseDto.builder()
                .statusCode(statusCode)
                .message("존재하지 않는 사용자 이메일입니다.")
                .fieldName("email")
                .build();

        log.error("[exceptionHandle] BadCredentialsException = {}", exceptionResponseDto.toString());
        return new ResponseEntity(exceptionResponseDto, StatusCode.UNAUTHORIZED_USER.getHttpStatus());
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity handleInvalidFormatException(InvalidFormatException e) {

        StatusCode statusCode = StatusCode.INVALID_PARAMETER;

        StatusResponseDto exceptionResponseDto = StatusResponseDto.builder()
                .statusCode(statusCode)
                .message(e.getTargetType().getName() + " 형식이 올바르지 않습니다.")
                .fieldName(e.getPath().get(0).getFieldName())
                .rejectValue(e.getValue().toString())
                .build();

        log.error("[exceptionHandle] InvalidFormatException = {}", exceptionResponseDto.toString());
        return new ResponseEntity(exceptionResponseDto, StatusCode.INVALID_PARAMETER.getHttpStatus());
    }


    //요청된 데이터와 정의된 데이터의 type이 다를 경우
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {

        StatusCode statusCode = StatusCode.INVALID_PARAMETER;

        StatusResponseDto exceptionResponseDto = StatusResponseDto.builder()
                .statusCode(statusCode)
                .message(e.getRequiredType().getName() + " 형식이 올바르지 않습니다.")
                .fieldName(e.getName())
                .rejectValue(e.getValue().toString())
                .build();

        log.error("[exceptionHandle] MethodArgumentTypeMismatchException = {}", exceptionResponseDto.toString());
        return new ResponseEntity(exceptionResponseDto, StatusCode.INVALID_PARAMETER.getHttpStatus());
    }



}
