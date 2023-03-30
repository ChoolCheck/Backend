package com.uhyeah.choolcheck.global.jwt;

import com.uhyeah.choolcheck.global.exception.CustomException;
import com.uhyeah.choolcheck.global.exception.StatusCode;
import com.uhyeah.choolcheck.web.user.CustomUserDetailsService;
import com.uhyeah.choolcheck.web.user.dto.TokenResponseDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
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
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    public static final String BEARER_PREFIX = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 1;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 3;
    private static final long MAIL_TOKEN_EXPIRE_TIME = 1000 * 60 * 10;

    private final Key key;
    private final CustomUserDetailsService customUserDetailsService;
    private final RedisTemplate<String, String> redisTemplate;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey, CustomUserDetailsService customUserDetailsService, RedisTemplate redisTemplate) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.customUserDetailsService = customUserDetailsService;
        this.redisTemplate = redisTemplate;
    }

    public String issueAccessToken(Authentication authentication) {

        long now = (new Date()).getTime();
        Date tokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(tokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String issueRefreshToken(Authentication authentication) {

        long now = (new Date()).getTime();
        Date tokenExpiresIn = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setExpiration(tokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String issueMailToken(Authentication authentication) {

        long now = (new Date()).getTime();
        Date tokenExpiresIn = new Date(now + MAIL_TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .setExpiration(tokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public TokenResponseDto reissueAccessToken(String accessToken, String refreshToken) {

        Claims claims = parseClaims(accessToken);
        String refreshTokenRedis = redisTemplate.opsForValue().get(claims.getSubject());

        if(refreshTokenRedis == null) {
            log.info("expired refreshToken");
            throw CustomException.builder()
                    .statusCode(StatusCode.UNAUTHORIZED_USER)
                    .message("만료된 refreshToken 입니다.")
                    .build();

        }
        if (!refreshTokenRedis.equals(refreshToken)) {
            throw CustomException.builder()
                    .statusCode(StatusCode.UNAUTHORIZED_USER)
                    .message("refreshToken이 일치하지 않습니다.")
                    .build();
        }

        Authentication authentication = getAuthentication(accessToken);
        String newAccessToken = issueAccessToken(authentication);
        log.info("reissue accessToken");

        return TokenResponseDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();

    }

    public TokenResponseDto generateTokenDto(Authentication authentication) {

        String accessToken = issueAccessToken(authentication);
        String refreshToken = issueRefreshToken(authentication);

        return TokenResponseDto.builder()
                 .grantType(BEARER_TYPE)
                 .accessToken(accessToken)
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

        UserDetails principal = customUserDetailsService.loadUserByUsername(claims.getSubject());
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
            throw CustomException.builder()
                    .statusCode(StatusCode.UNAUTHORIZED_USER)
                    .message("expired")
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

    public Claims parseClaims(String accessToken) {

        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
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

}
