package com.uhyeah.choolcheck.web.user;

import com.uhyeah.choolcheck.domain.repository.UserRepository;
import com.uhyeah.choolcheck.web.exception.CustomException;
import com.uhyeah.choolcheck.web.exception.StatusCode;
import com.uhyeah.choolcheck.web.user.dto.TokenResponseDto;
import com.uhyeah.choolcheck.web.user.dto.UserLoginRequestDto;
import com.uhyeah.choolcheck.web.user.dto.UserSaveRequestDto;
import com.uhyeah.choolcheck.web.user.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManagerBuilder managerBuilder;

    @Transactional
    public Long signup(UserSaveRequestDto userSaveRequestDto) {

        if (userRepository.existsByEmail(userSaveRequestDto.getEmail())) {
            throw new CustomException(StatusCode.DUPLICATE_RESOURCE, "[User] email : "+userSaveRequestDto.getEmail());
        }

        return userRepository.save(userSaveRequestDto.toEntity(passwordEncoder)).getId();
    }

    @Transactional(readOnly = true)
    public TokenResponseDto login(UserLoginRequestDto userLoginRequestDto) {

        UsernamePasswordAuthenticationToken authenticationToken = userLoginRequestDto.toAuthentication();
        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
        log.info("authentication = {}", authentication);
        return tokenProvider.generateTokenDto(authentication);
    }
}
