package com.uhyeah.choolcheck.web.user.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uhyeah.choolcheck.web.exception.CustomException;
import com.uhyeah.choolcheck.web.exception.StatusCode;
import com.uhyeah.choolcheck.web.exception.StatusResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        log.error("[JwtAuthenticationEntryPoint] request exception = {}",request.getAttribute("exception"));


        if (authException.getClass().equals(BadCredentialsException.class)) {
            setResponse(response, new StatusResponseDto(StatusCode.UNAUTHORIZED_USER, "비밀번호가 일치하지 않습니다."));
        }

        else if(authException.getClass().equals(UsernameNotFoundException.class)) {
            setResponse(response, new StatusResponseDto(StatusCode.UNAUTHORIZED_USER, "존재하지 않는 사용자 이메일입니다."));
        }
    }

    public void setResponse(HttpServletResponse response, StatusResponseDto statusResponseDto) throws IOException{

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getOutputStream(), statusResponseDto);
        response.setStatus(StatusCode.UNAUTHORIZED_USER.getHttpStatus().value()); //statuscode수정요망

    }
}
