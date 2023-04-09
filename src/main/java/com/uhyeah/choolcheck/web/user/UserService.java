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

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManagerBuilder managerBuilder;
    private final RedisService redisService;
    private final MailService mailService;


    @Transactional
    public void signup(UserSaveRequestDto userSaveRequestDto) {

        checkEmailDuplication(userSaveRequestDto.getEmail());

        String code = redisService.get(userSaveRequestDto.getEmail());
        verifyCode(userSaveRequestDto.getCode(), code);

        userRepository.save(userSaveRequestDto.toEntity(passwordEncoder));
    }


    public void sendVerifyEmailMail(EmailValidateRequestDto emailValidateRequestDto) {

        final long EMAIL_CODE_EXPIRATION_MINUTES = 5;

        String code = createCode();
        String email = emailValidateRequestDto.getEmail();
        String title = "[출첵] 이메일 인증 메일입니다.";
        String content = "인증번호 : " + code +
                "\n 해당 인증번호는 5분간 유효합니다.";

        redisService.set(email, code, Duration.ofMinutes(EMAIL_CODE_EXPIRATION_MINUTES));
        mailService.sendMail(email, title, content);
    }


    @Transactional(readOnly = true)
    public TokenResponseDto login(UserLoginRequestDto userLoginRequestDto, String ip) {

        UsernamePasswordAuthenticationToken authenticationToken = userLoginRequestDto.toAuthentication();
        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
        TokenResponseDto tokenResponseDto = tokenProvider.generateTokenDto(authentication);

        long expiration = tokenProvider.getExpiration(tokenResponseDto.getRefreshToken());
        redisService.set(tokenResponseDto.getRefreshToken(), ip, Duration.ofMillis(expiration));
        
        return tokenResponseDto;
    }


    public TokenResponseDto reissue(String accessToken, String refreshToken, String ip) {
        System.out.println(ip);
        return tokenProvider.reissueAccessToken(accessToken, refreshToken, ip);
    }


    public void logout(String bearerToken, CustomUserDetails customUserDetails) {

        String accessToken = tokenProvider.resolveToken(bearerToken);
        long expiration = tokenProvider.getExpiration(accessToken);

        redisService.delete(customUserDetails.getUser().getEmail());
        redisService.set(accessToken, "logout", Duration.ofMillis(expiration));

        SecurityContextHolder.clearContext();
    }


    @Transactional
    public void update(UserUpdateRequestDto userUpdateRequestDto, CustomUserDetails customUserDetails) {

        User user = userRepository.findById(customUserDetails.getUser().getId())
                .orElseThrow(() -> CustomException.builder()
                        .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                        .message("존재하지 않는 유저입니다.")
                        .fieldName("email")
                        .rejectValue(customUserDetails.getUsername())
                        .build());

        user.setStoreName(userUpdateRequestDto.getStoreName());
    }


    public void sendUpdatePasswordMail(String bearerToken, CustomUserDetails customUserDetails) {

        String accessToken = tokenProvider.resolveToken(bearerToken);
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        String mailToken = tokenProvider.issueMailToken(authentication);

        final String PASSWORD_UPDATE_URL =  SiteProperties.SITE_URL +"/password?token=" + mailToken;

        String email = customUserDetails.getUsername();
        String title = "[출첵] 비밀번호 변경 메일입니다.";
        String content = "비밀번호 변경 url : " + PASSWORD_UPDATE_URL
                +"\n 해당 링크는 5분간 유효합니다.";

        mailService.sendMail(email, title, content);
    }

    @Transactional
    public void updatePassword(PasswordUpdateRequestDto passwordUpdateRequestDto, CustomUserDetails customUserDetails) {

        User user = userRepository.findByEmail(customUserDetails.getUsername())
                .orElseThrow(() -> CustomException.builder()
                        .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                        .message("존재하지 않는 유저입니다.")
                        .fieldName("email")
                        .rejectValue(customUserDetails.getUsername())
                        .build());

        user.setPassword(passwordEncoder.encode(passwordUpdateRequestDto.getPassword()));
    }


    @Transactional
    public void delete(CustomUserDetails customUserDetails) {

        userRepository.delete(customUserDetails.getUser());
    }


    public UserResponseDto getUser(CustomUserDetails customUserDetails) {

        return new UserResponseDto(customUserDetails.getUser());
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


    public String createCode() {

        StringBuilder code = new StringBuilder();
        Random random = new Random();

        for (int i=0; i<6; i++) {
            code.append((random.nextInt(10)));
        }
        return code.toString();
    }


    private void verifyCode(String requestCode, String code) {

        if (code == null || !code.equals(requestCode)) {
            throw CustomException.builder()
                    .statusCode(StatusCode.INVALID_PARAMETER)
                    .message("일지하지 않는 인증번호입니다..")
                    .fieldName("code")
                    .rejectValue(requestCode)
                    .build();
        }
    }
}
