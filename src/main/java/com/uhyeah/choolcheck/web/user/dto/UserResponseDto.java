package com.uhyeah.choolcheck.web.user.dto;

import com.uhyeah.choolcheck.domain.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponseDto {

    private String email;

    private String storeName;

    public UserResponseDto(User user) {
        this.email = user.getEmail();
        this.storeName = user.getStoreName();
    }
}
