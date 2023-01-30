package com.uhyeah.choolcheck.web.user.jwt;

import com.uhyeah.choolcheck.domain.entity.User;
import com.uhyeah.choolcheck.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("CustomUserDetailsService");
        return userRepository.findByEmail(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(username + "없음"));
    }

    private UserDetails createUserDetails(User user) {

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getAuthority().toString());

        return new org.springframework.security.core.userdetails.User(
                String.valueOf(user.getId()),
                user.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }
}
