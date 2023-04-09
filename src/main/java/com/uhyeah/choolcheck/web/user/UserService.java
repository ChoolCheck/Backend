package com.uhyeah.choolcheck.web.user;

import com.uhyeah.choolcheck.domain.entity.User;
import com.uhyeah.choolcheck.domain.repository.UserRepository;
import com.uhyeah.choolcheck.global.MailService;
import com.uhyeah.choolcheck.global.RedisService;
import com.uhyeah.choolcheck.global.SiteProperties;
import com.uhyeah.choolcheck.global.exception.CustomException;
import com.uhyeah.choolcheck.global.exception.StatusCode;
import com.uhyeah.choolcheck.global.jwt.JwtTokenProvider;
import com.uhyeah.choolcheck.web.user.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class UserService {

    private static final long EMAIL_CODE_EXPIRATION_MINUTES = 5;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManagerBuilder managerBuilder;
    private final RedisService redisService;
    private final MailService mailService;


    @Transactional
    public void signup(UserSaveRequestDto requestDto) {

        checkEmailDuplication(requestDto.getEmail());
        validateCode(requestDto.getEmail(), requestDto.getCode());

        userRepository.save(requestDto.toEntity(passwordEncoder));
    }


    @Transactional(readOnly = true)
    public TokenResponseDto login(UserLoginRequestDto requestDto, String ip) {

        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();
        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
        TokenResponseDto tokenResponseDto = tokenProvider.generateTokenDto(authentication);

        long refreshTokenExpiration = tokenProvider.getExpiration(tokenResponseDto.getRefreshToken());
        redisService.set(tokenResponseDto.getRefreshToken(), ip, Duration.ofMillis(refreshTokenExpiration));

        return tokenResponseDto;
    }


    public TokenResponseDto reissue(String accessToken, String refreshToken, String ip) {

        return tokenProvider.reissueAccessToken(accessToken, refreshToken, ip);
    }


    public void logout(String bearerToken) {

        String accessToken = tokenProvider.resolveToken(bearerToken);
        long expiration = tokenProvider.getExpiration(accessToken);

        redisService.set(accessToken, "logout", Duration.ofMillis(expiration));

        SecurityContextHolder.clearContext();
    }


    @Transactional
    public void update(UserUpdateRequestDto requestDto, User loginUser) {

        User user = userRepository.findById(loginUser.getId())
                .orElseThrow(() -> CustomException.builder()
                        .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                        .message("존재하지 않는 유저입니다.")
                        .fieldName("email")
                        .rejectValue(loginUser.getEmail())
                        .build());

        user.setStoreName(requestDto.getStoreName());
    }


    public void sendVerifyEmailMail(EmailValidateRequestDto requestDto) {

        final String code = createCode();
        final String email = requestDto.getEmail();
        final String title = "[출첵] 이메일 인증 메일입니다.";
        final String content = "인증번호 : " + code +
                "\n 해당 인증번호는" + EMAIL_CODE_EXPIRATION_MINUTES +"분간 유효합니다.";

        mailService.sendMail(email, title, content);

        redisService.set(email, code, Duration.ofMinutes(EMAIL_CODE_EXPIRATION_MINUTES));
    }


    public void sendUpdatePasswordMail(String bearerToken, User loginUser) {

        String accessToken = tokenProvider.resolveToken(bearerToken);
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        
        String mailToken = tokenProvider.issueMailToken(authentication);

        final String PASSWORD_UPDATE_URL =  SiteProperties.SITE_URL +"/password?token=" + mailToken;
        final String email = loginUser.getEmail();
        final String title = "[출첵] 비밀번호 변경 메일입니다.";
        final String content = "비밀번호 변경 url : " + PASSWORD_UPDATE_URL
                +"\n 해당 링크는 5분간 유효합니다.";

        mailService.sendMail(email, title, content);
    }

    @Transactional
    public void updatePassword(PasswordUpdateRequestDto requestDto, User loginUser) {

        User user = userRepository.findByEmail(loginUser.getEmail())
                .orElseThrow(() -> CustomException.builder()
                        .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                        .message("존재하지 않는 유저입니다.")
                        .fieldName("email")
                        .rejectValue(loginUser.getEmail())
                        .build());

        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
    }


    @Transactional
    public void delete(User loginUser) {

        userRepository.delete(loginUser);
    }


    public UserResponseDto getUser(User loginUser) {

        return new UserResponseDto(loginUser);
    }


    private void checkEmailDuplication(String email) {

        if (userRepository.existsByEmail(email)) {
            throw CustomException.builder()
                    .statusCode(StatusCode.DUPLICATE_RESOURCE)
                    .message("중복된 이메일입니다.")
                    .fieldName("email")
                    .rejectValue(email)
                    .build();
        }
    }


    private void validateCode(String email, String requestCode) {

        String code = redisService.get(email);

        if (code == null || !code.equals(requestCode)) {
            throw CustomException.builder()
                    .statusCode(StatusCode.INVALID_PARAMETER)
                    .message("일지하지 않는 인증번호입니다..")
                    .fieldName("code")
                    .rejectValue(requestCode)
                    .build();
        }
    }


    private String createCode() {

        StringBuilder code = new StringBuilder();
        Random random = new Random();

        for (int i=0; i<6; i++) {
            code.append((random.nextInt(10)));
        }
        return code.toString();
    }
}
