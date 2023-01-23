package com.uhyeah.choolcheck.web.user;

import com.uhyeah.choolcheck.domain.entity.User;
import com.uhyeah.choolcheck.domain.repository.UserRepository;
import com.uhyeah.choolcheck.web.user.dto.TokenResponseDto;
import com.uhyeah.choolcheck.web.user.dto.UserLoginRequestDto;
import com.uhyeah.choolcheck.web.user.dto.UserSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;

    @Transactional
    public Long signup(UserSaveRequestDto userSaveRequestDto) {

        if (userRepository.existsByEmail(userSaveRequestDto.getEmail())) {
            return null;
        }

        userSaveRequestDto.setPasswordEncoded(passwordEncoder.encode(userSaveRequestDto.getPassword()));
        return userRepository.save(userSaveRequestDto.toEntity()).getId();
    }

    @Transactional(readOnly = true)
    public TokenResponseDto login(UserLoginRequestDto userLoginRequestDto) {

        User user = userRepository.findByEmail(userLoginRequestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("[User] email : "+userLoginRequestDto.getEmail()));

        if (!passwordEncoder.matches(userLoginRequestDto.getPassword(), user.getPassword())) {
            return null;
        }
        return tokenProvider.;
    }
}
