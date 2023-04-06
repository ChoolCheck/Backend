package com.uhyeah.choolcheck.web.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenResponseDto {

    private final String grantType;
    private final String accessToken;
    private final String refreshToken;
}
