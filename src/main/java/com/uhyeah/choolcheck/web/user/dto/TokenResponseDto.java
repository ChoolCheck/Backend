package com.uhyeah.choolcheck.web.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TokenResponseDto {

    private String accessToken;

    @Builder
    public TokenResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
