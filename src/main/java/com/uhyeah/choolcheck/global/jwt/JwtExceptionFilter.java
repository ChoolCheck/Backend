package com.uhyeah.choolcheck.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uhyeah.choolcheck.global.exception.CustomException;
import com.uhyeah.choolcheck.global.exception.StatusCode;
import com.uhyeah.choolcheck.global.exception.StatusResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (CustomException e) {
            setResponse(response, e.getMessage());
        }
    }

    private void setResponse(HttpServletResponse response, String message) throws IOException {

        log.error("[exceptionHandle] JwtException e = {}", message);

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
