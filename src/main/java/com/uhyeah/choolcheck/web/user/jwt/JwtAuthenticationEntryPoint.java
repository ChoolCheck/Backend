package com.uhyeah.choolcheck.web.user.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uhyeah.choolcheck.web.exception.StatusCode;
import com.uhyeah.choolcheck.web.exception.StatusResponseDto;
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
        setResponse(response, "유저 인증정보가 없습니다.");
    }

    private void setResponse(HttpServletResponse response, String message) throws IOException {

        log.error("[exceptionHandle] AuthenticationEntryPoint = {}", message);

        StatusCode statusCode = StatusCode.UNAUTHORIZED_USER;
        ObjectMapper objectMapper = new ObjectMapper();

        response.setStatus(statusCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");

        StatusResponseDto statusResponseDto = StatusResponseDto.builder()
                .statusCode(statusCode)
                .message(message)
                .build();
        response.getWriter().write(objectMapper.writeValueAsString(statusResponseDto));
    }
}
