package com.uhyeah.choolcheck.web.user.dto;

import com.uhyeah.choolcheck.domain.entity.User;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserResponseDto {

    private final String email;

    private final String storeName;

    private final LocalDate createdDate;

    public UserResponseDto(User user) {
        this.email = user.getEmail();
        this.storeName = user.getStoreName();
        this.createdDate = user.getCreatedDate();
    }
}
