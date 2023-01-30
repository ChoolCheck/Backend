package com.uhyeah.choolcheck.web.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static Long getCurrentMemberId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Securiy Context에 인증정보가 없습니다.");
        }

        return Long.parseLong(authentication.getName());
    }

}
