package com.uhyeah.choolcheck.web.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class TokenResponseDto {

    private String grantType;
    private String accessToken;
    private String refreshToken;

    @Builder
    public TokenResponseDto(String grantType, String accessToken, String refreshToken) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
