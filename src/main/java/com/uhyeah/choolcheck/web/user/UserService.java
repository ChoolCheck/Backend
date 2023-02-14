package com.uhyeah.choolcheck.web.user;

import com.uhyeah.choolcheck.domain.entity.User;
import com.uhyeah.choolcheck.domain.repository.UserRepository;
import com.uhyeah.choolcheck.web.exception.CustomException;
import com.uhyeah.choolcheck.web.exception.StatusCode;
import com.uhyeah.choolcheck.web.user.dto.*;
import com.uhyeah.choolcheck.web.user.jwt.JwtTokenProvider;
import com.uhyeah.choolcheck.web.user.redis.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManagerBuilder managerBuilder;
    private final RedisRepository redisRepository;
    private final JavaMailSender javaMailSender;

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

        userRepository.save(userSaveRequestDto.toEntity(passwordEncoder));
    }

    @Transactional(readOnly = true)
    public TokenResponseDto login(UserLoginRequestDto userLoginRequestDto) {

        UsernamePasswordAuthenticationToken authenticationToken = userLoginRequestDto.toAuthentication();
        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
        TokenResponseDto tokenResponseDto = tokenProvider.generateTokenDto(authentication);
        redisRepository.setValues(userLoginRequestDto.getEmail(), tokenResponseDto.getRefreshToken(), Duration.ofMinutes(2));

        return tokenResponseDto;
    }

    @Transactional(readOnly = true)
    public TokenResponseDto reissue(String refreshToken) {

        return tokenProvider.reissueAccessToken(refreshToken);
    }


    @Transactional(readOnly = true)
    public void logout(String bearerToken, CustomUserDetails customUserDetails) {

        String accessToken = tokenProvider.resolveToken(bearerToken);
        long expiration = tokenProvider.getExpiration(accessToken);
        System.out.println(accessToken);
        redisRepository.deleteValues(customUserDetails.getUser().getEmail());
        redisRepository.setValues(accessToken, "logout", Duration.ofMillis(expiration));

        SecurityContextHolder.clearContext();
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUser(CustomUserDetails customUserDetails) {

        return new UserResponseDto(customUserDetails.getUser());
    }

    @Transactional
    public void update(UserUpdateRequestDto userUpdateRequestDto, CustomUserDetails customUserDetails) {

        User user = customUserDetails.getUser();
        user.setStoreName(userUpdateRequestDto.getStoreName());
    }

    public void sendUpdatePasswordEmail(CustomUserDetails customUserDetails) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        try {
            String receive = customUserDetails.getUsername();

            simpleMailMessage.setTo(receive);
            simpleMailMessage.setSubject("출첵 비밀번호 변경메일");
            simpleMailMessage.setText("메일내용");

            javaMailSender.send(simpleMailMessage);
        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    @Transactional
    public void updatePassword(UserPasswordUpdateRequestDto userPasswordUpdateRequestDto, CustomUserDetails customUserDetails) {

        User user = customUserDetails.getUser();
        user.setPassword(passwordEncoder.encode(userPasswordUpdateRequestDto.getPassword()));
    }

    @Transactional
    public void delete(CustomUserDetails customUserDetails) {

        userRepository.delete(customUserDetails.getUser());
    }
}
