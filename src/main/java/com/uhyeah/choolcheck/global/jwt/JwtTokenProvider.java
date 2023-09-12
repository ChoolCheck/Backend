package com.uhyeah.choolcheck.global.jwt;

import com.uhyeah.choolcheck.global.RedisUtil;
import com.uhyeah.choolcheck.global.exception.CustomException;
import com.uhyeah.choolcheck.global.exception.StatusCode;
import com.uhyeah.choolcheck.web.user.CustomUserDetailsService;
import com.uhyeah.choolcheck.web.user.dto.TokenResponseDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private static final String BEARER_TYPE = "bearer";
    private static final String BEARER_PREFIX = "Bearer";
    private static final String AUTHORITIES_KEY = "auth";

    private static final String ACCESS_TOKEN_SUBJECT = "access";
    private static final String REFRESH_TOKEN_SUBJECT = "refresh";
    private static final String MAIL_TOKEN_SUBJECT = "mail";

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60; // 1 hour
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 14; // 2 week
    private static final long MAIL_TOKEN_EXPIRE_TIME = 1000 * 60 * 5; // 5 minute

    private final Key key;
    private final CustomUserDetailsService customUserDetailsService;
    private final RedisUtil redisUtil;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey, CustomUserDetailsService customUserDetailsService, RedisUtil redisUtil) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.customUserDetailsService = customUserDetailsService;
        this.redisUtil = redisUtil;
    }


    public TokenResponseDto generateTokenDto(Authentication authentication) {

        String accessToken = issueAccessToken(authentication);
        String refreshToken = issueRefreshToken();

        return TokenResponseDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    public String issueMailToken(Authentication authentication) {

        Date tokenExpiresIn = getTokenExpiresIn(MAIL_TOKEN_EXPIRE_TIME);
        String authorities = getAuthorities(authentication);

        return Jwts.builder()
                .setSubject(MAIL_TOKEN_SUBJECT)
                .setAudience(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(tokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }


    private String issueAccessToken(Authentication authentication) {

        Date tokenExpiresIn = getTokenExpiresIn(ACCESS_TOKEN_EXPIRE_TIME);
        String authorities = getAuthorities(authentication);

        return Jwts.builder()
                .setSubject(ACCESS_TOKEN_SUBJECT)
                .setAudience(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(tokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }


    private String issueRefreshToken() {

        Date tokenExpiresIn = getTokenExpiresIn(REFRESH_TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setSubject(REFRESH_TOKEN_SUBJECT)
                .setExpiration(tokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }



    public TokenResponseDto reissueAccessToken(String accessToken, String refreshToken, String reissueIp) {

        String ip = redisUtil.get(refreshToken);

        validateToken(refreshToken);

        if (ip != null && !ip.equals(reissueIp)) {
            throw CustomException.builder()
                    .statusCode(StatusCode.UNAUTHORIZED_USER)
                    .message("refreshToken이 일치하지 않습니다.")
                    .build();
        }

        Authentication authentication = getAuthentication(accessToken);
        String newAccessToken = issueAccessToken(authentication);

        return TokenResponseDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();

    }


    public Authentication getAuthentication(String token) {

        Claims claims = parseClaims(token);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = customUserDetailsService.loadUserByUsername(claims.getAudience());
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public long getExpiration(String token) {

        Claims claims = parseClaims(token);

        long now = (new Date()).getTime();
        return claims.getExpiration().getTime() - now;
    }


    public void validateToken(String token) {

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        }
        catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw CustomException.builder()
                    .statusCode(StatusCode.UNAUTHORIZED_USER)
                    .message("잘못된 JWT 서명입니다.")
                    .build();

        } catch (ExpiredJwtException e) {

            String message = e.getClaims().getSubject() + " token expired";

            throw CustomException.builder()
                    .statusCode(StatusCode.UNAUTHORIZED_USER)
                    .message(message)
                    .build();

        } catch (UnsupportedJwtException e) {
            throw CustomException.builder()
                    .statusCode(StatusCode.UNAUTHORIZED_USER)
                    .message("지원되지 않는 JWT 토큰입니다.")
                    .build();

        } catch (IllegalArgumentException e) {
            throw CustomException.builder()
                    .statusCode(StatusCode.UNAUTHORIZED_USER)
                    .message("JWT 토큰이 잘못되었습니다.")
                    .build();
        }
    }

    private Claims parseClaims(String token) {

        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }


    public String resolveToken(String bearerToken) {

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX) && bearerToken.length() > 6) {
            return bearerToken.substring(7);
        }
        return null;
    }


    private Date getTokenExpiresIn(long tokenExpireTime) {
        long now = (new Date()).getTime();
        return new Date(now + tokenExpireTime);
    }


    private String getAuthorities(Authentication authentication) {

        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }
}
