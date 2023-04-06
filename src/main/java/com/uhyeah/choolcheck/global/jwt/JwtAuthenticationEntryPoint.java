package com.uhyeah.choolcheck.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uhyeah.choolcheck.global.exception.StatusCode;
import com.uhyeah.choolcheck.global.exception.StatusResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        setResponse(response);
    }

    private void setResponse(HttpServletResponse response) throws IOException {

        final String ERROR_MESSAGE = "유저 인증정보가 없습니다.";

        log.error("[exceptionHandle] AuthenticationEntryPoint = {}", ERROR_MESSAGE);

        StatusCode statusCode = StatusCode.UNAUTHORIZED_USER;
        ObjectMapper objectMapper = new ObjectMapper();

        response.setStatus(statusCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");

        StatusResponseDto statusResponseDto = StatusResponseDto.builder()
                .statusCode(statusCode)
                .message(ERROR_MESSAGE)
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(statusResponseDto));
    }
}
