package com.uhyeah.choolcheck.web.user;

import com.uhyeah.choolcheck.domain.repository.UserRepository;
import com.uhyeah.choolcheck.web.user.dto.UserSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long signup(UserSaveRequestDto userSaveRequestDto) {

        if (userRepository.existsByEmail(userSaveRequestDto.getEmail())) {
            return null;
        }

        userSaveRequestDto.setPasswordEncoded(passwordEncoder.encode(userSaveRequestDto.getPassword()));
        return userRepository.save(userSaveRequestDto.toEntity()).getId();
    }
}
