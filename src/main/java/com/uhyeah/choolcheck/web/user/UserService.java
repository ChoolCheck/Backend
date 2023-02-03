package com.uhyeah.choolcheck.web.user;

import com.uhyeah.choolcheck.domain.entity.User;
import com.uhyeah.choolcheck.domain.repository.UserRepository;
import com.uhyeah.choolcheck.web.exception.CustomException;
import com.uhyeah.choolcheck.web.exception.StatusCode;
import com.uhyeah.choolcheck.web.user.dto.*;
import com.uhyeah.choolcheck.web.user.jwt.CustomUserDetails;
import com.uhyeah.choolcheck.web.user.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManagerBuilder managerBuilder;

    @Transactional
    public void signup(UserSaveRequestDto userSaveRequestDto) {

        if (userRepository.existsByEmail(userSaveRequestDto.getEmail())) {
            throw new CustomException(StatusCode.DUPLICATE_RESOURCE, "[User] email : "+userSaveRequestDto.getEmail());
        }

        userRepository.save(userSaveRequestDto.toEntity(passwordEncoder));
    }

    @Transactional(readOnly = true)
    public TokenResponseDto login(UserLoginRequestDto userLoginRequestDto) {

        UsernamePasswordAuthenticationToken authenticationToken = userLoginRequestDto.toAuthentication();
        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
        return tokenProvider.generateTokenDto(authentication);
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
