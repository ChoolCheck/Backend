package com.uhyeah.choolcheck.global.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        JwtAuthenticationFilter customFilter = new JwtAuthenticationFilter(jwtTokenProvider, redisTemplate);
        builder.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
        builder.addFilterBefore(new JwtExceptionFilter(), JwtAuthenticationFilter.class);
    }
}
