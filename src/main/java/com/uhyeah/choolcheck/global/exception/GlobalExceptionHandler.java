package com.uhyeah.choolcheck.global.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
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

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<StatusResponseDto> handleCustomException(CustomException e) {

        StatusResponseDto exceptionResponseDto = StatusResponseDto.builder()
                .statusCode(e.getStatusCode())
                .message(e.getMessage())
                .fieldName(e.getFieldName())
                .rejectValue(e.getRejectValue())
                .build();

        log.error("[exceptionHandle] CustomException " + exceptionResponseDto.toString());

        return ResponseEntity.status(e.getStatusCode().getHttpStatus()).body(exceptionResponseDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<StatusResponseDto>> handleValidException(MethodArgumentNotValidException e) {

        final StatusCode statusCode = StatusCode.INVALID_PARAMETER;
        BindingResult bindingResult = e.getBindingResult();

        List<StatusResponseDto> exceptionResponseDtoList = new ArrayList<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {

            String rejectValue = null;
            if (fieldError.getRejectedValue() != null) {
                rejectValue = fieldError.getRejectedValue().toString();
            }
            exceptionResponseDtoList.add(StatusResponseDto.builder()
                    .statusCode(statusCode)
                    .message(fieldError.getDefaultMessage())
                    .fieldName(fieldError.getField())
                    .rejectValue(rejectValue)
                    .build());
        }

        log.error("[exceptionHandle] MethodArgumentNotValidException" + exceptionResponseDtoList.toString());
        return ResponseEntity.status(statusCode.getHttpStatus()).body(exceptionResponseDtoList);
    }


    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<StatusResponseDto> handleInvalidFormatException(InvalidFormatException e) {

        final StatusCode statusCode = StatusCode.INVALID_PARAMETER;

        StatusResponseDto exceptionResponseDto = StatusResponseDto.builder()
                .statusCode(statusCode)
                .message(e.getTargetType().getName() + " 형식이 올바르지 않습니다.")
                .fieldName(e.getPath().get(0).getFieldName())
                .rejectValue(e.getValue().toString())
                .build();

        log.error("[exceptionHandle] InvalidFormatException = {}", exceptionResponseDto.toString());

        return ResponseEntity.status(statusCode.getHttpStatus()).body(exceptionResponseDto);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<StatusResponseDto> handleDateTimeParseException(DateTimeParseException e) {

        final StatusCode statusCode = StatusCode.INVALID_PARAMETER;

        StatusResponseDto exceptionResponseDto = StatusResponseDto.builder()
                .statusCode(statusCode)
                .message("시간 형식이 올바르지 않습니다.")
                .fieldName("period")
                .rejectValue(e.getParsedString())
                .build();

        log.error("[exceptionHandle] InvalidFormatException = {}", exceptionResponseDto.toString());

        return ResponseEntity.status(statusCode.getHttpStatus()).body(exceptionResponseDto);
    }

    //요청된 데이터와 정의된 데이터의 type이 다를 경우
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<StatusResponseDto> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {

        final StatusCode statusCode = StatusCode.INVALID_PARAMETER;

        StatusResponseDto exceptionResponseDto = StatusResponseDto.builder()
                .statusCode(statusCode)
                .message(e.getRequiredType().getName() + " 형식이 올바르지 않습니다.")
                .fieldName(e.getName())
                .rejectValue(e.getValue().toString())
                .build();

        log.error("[exceptionHandle] MethodArgumentTypeMismatchException = {}", exceptionResponseDto.toString());

        return ResponseEntity.status(statusCode.getHttpStatus()).body(exceptionResponseDto);
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<StatusResponseDto> handleBadCredentialsException(BadCredentialsException e) {

        final StatusCode statusCode = StatusCode.UNAUTHORIZED_USER;
        StatusResponseDto exceptionResponseDto = StatusResponseDto.builder()
                .statusCode(statusCode)
                .message("비밀번호가 일치하지 않습니다.")
                .fieldName("password")
                .build();

        log.error("[exceptionHandle] BadCredentialsException = {}", exceptionResponseDto.toString());

        return ResponseEntity.status(statusCode.getHttpStatus()).body(exceptionResponseDto);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<StatusResponseDto> handleUsernameNotFoundException(UsernameNotFoundException e) {

        final StatusCode statusCode = StatusCode.UNAUTHORIZED_USER;
        StatusResponseDto exceptionResponseDto = StatusResponseDto.builder()
                .statusCode(statusCode)
                .message("존재하지 않는 사용자 이메일입니다.")
                .fieldName("email")
                .build();

        log.error("[exceptionHandle] BadCredentialsException = {}", exceptionResponseDto.toString());

        return ResponseEntity.status(statusCode.getHttpStatus()).body(exceptionResponseDto);
    }

//    @ExceptionHandler(JwtException.class)
//    public ResponseEntity<StatusResponseDto> handleJwtException(JwtException e) {
//
//        final StatusCode statusCode = StatusCode.UNAUTHORIZED_USER;
//        String message = "JWT토큰 오류입니다.";
//
//        if (e.equals(MalformedJwtException.class)) {
//            message = "올바르지 않은 형식의 JWT토큰입니다.";
//        }
//        if (e.equals(ExpiredJwtException.class)) {
//            message = "만료된 JWT토큰입니다.";
//        }
//        if (e.equals(UnsupportedJwtException.class)) {
//            message = "지원하지않는 JWT토큰입니다.";
//        }
//
//        StatusResponseDto exceptionResponseDto = StatusResponseDto.builder()
//                .statusCode(statusCode)
//                .message(message)
//                .build();
//
//        log.error("[exceptionHandle] JwtException = {}", exceptionResponseDto.toString());
//        return new ResponseEntity(exceptionResponseDto, statusCode.getHttpStatus());
//    }
}
