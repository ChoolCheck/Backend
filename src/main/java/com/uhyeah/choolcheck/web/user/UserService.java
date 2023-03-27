package com.uhyeah.choolcheck.web.user;

import com.uhyeah.choolcheck.domain.entity.User;
import com.uhyeah.choolcheck.domain.repository.UserRepository;
import com.uhyeah.choolcheck.global.exception.CustomException;
import com.uhyeah.choolcheck.global.exception.StatusCode;
import com.uhyeah.choolcheck.web.user.dto.*;
import com.uhyeah.choolcheck.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManagerBuilder managerBuilder;
    private final RedisTemplate<String, String> redisTemplate;
    private final MailService mailService;

    @Transactional
    public void signup(UserSaveRequestDto userSaveRequestDto) {

        if (userRepository.existsByEmail(userSaveRequestDto.getEmail())) {
            throw CustomException.builder()
                    .statusCode(StatusCode.DUPLICATE_RESOURCE)
                    .message("중복된 이메일입니다.")
                    .fieldName("email")
                    .rejectValue(userSaveRequestDto.getEmail())
                    .build();
        }

        String email = redisTemplate.opsForValue().get(userSaveRequestDto.getCode());
        if (!email.equals(userSaveRequestDto.getEmail())) {
            throw CustomException.builder()
                    .statusCode(StatusCode.INVALID_PARAMETER)
                    .message("일지하지 않는 인증번호입니다..")
                    .fieldName("code")
                    .rejectValue(userSaveRequestDto.getCode())
                    .build();
        }

        userRepository.save(userSaveRequestDto.toEntity(passwordEncoder));
    }

    public void verifyEmail(EmailValidateRequestDto emailValidateRequestDto) {

        String code = createCode();
        String receive = emailValidateRequestDto.getEmail();
        String subject = "[출첵] 이메일 인증 메일입니다.";
        String text = "인증번호 : " + code;

        redisTemplate.opsForValue().set(code, receive);
        mailService.sendMail(receive, subject, text);
    }

    @Transactional(readOnly = true)
    public TokenResponseDto login(UserLoginRequestDto userLoginRequestDto) {

        UsernamePasswordAuthenticationToken authenticationToken = userLoginRequestDto.toAuthentication();
        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
        TokenResponseDto tokenResponseDto = tokenProvider.generateTokenDto(authentication);
        redisTemplate.opsForValue().set(userLoginRequestDto.getEmail(), tokenResponseDto.getRefreshToken(), Duration.ofDays(14));

        return tokenResponseDto;
    }

    @Transactional(readOnly = true)
    public TokenResponseDto reissue(String accessToken, String refreshToken) {

        return tokenProvider.reissueAccessToken(accessToken, refreshToken);
    }


    @Transactional(readOnly = true)
    public void logout(String bearerToken, CustomUserDetails customUserDetails) {

        String accessToken = tokenProvider.resolveToken(bearerToken);
        long expiration = tokenProvider.getExpiration(accessToken);
        System.out.println(accessToken);
        redisTemplate.delete(customUserDetails.getUser().getEmail());
        redisTemplate.opsForValue().set(accessToken, "logout", Duration.ofMillis(expiration));

        SecurityContextHolder.clearContext();
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUser(CustomUserDetails customUserDetails) {

        return new UserResponseDto(customUserDetails.getUser());
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

    public void sendUpdatePasswordEmail(String bearerToken, CustomUserDetails customUserDetails) {

        String accessToken = tokenProvider.resolveToken(bearerToken);
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        String mailToken = tokenProvider.issueAccessToken(authentication);

        final String url = "http://choolcheck-frontend.s3-website.ap-northeast-2.amazonaws.com/updatePassword?token=" + mailToken;

        String receive = customUserDetails.getUsername();
        String subject = "[출첵] 비밀번호 변경 메일입니다.";
        String text = "비밀번호 변경 url : " + url;

        mailService.sendMail(receive, subject, text);
    }

    @Transactional
    public void updatePassword(UserPasswordUpdateRequestDto userPasswordUpdateRequestDto, CustomUserDetails customUserDetails) {

        String email = customUserDetails.getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> CustomException.builder()
                        .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                        .message("존재하지 않는 유저입니다.")
                        .fieldName("email")
                        .rejectValue(email)
                        .build());

        user.setPassword(passwordEncoder.encode(userPasswordUpdateRequestDto.getPassword()));
    }

    @Transactional
    public void delete(CustomUserDetails customUserDetails) {

        userRepository.delete(customUserDetails.getUser());
    }

    public static String createCode() {

        StringBuffer code = new StringBuffer();
        Random random = new Random();

        for (int i=0; i<6; i++) {
            code.append((random.nextInt(10)));
        }
        return code.toString();
    }
}
