package com.uhyeah.choolcheck.web.user.dto;

import com.uhyeah.choolcheck.domain.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserResponseDto {

    private String email;

    private String storeName;

    private LocalDate createdDate;

    public UserResponseDto(User user) {
        this.email = user.getEmail();
        this.storeName = user.getStoreName();
        this.createdDate = user.getCreatedDate();
    }
}
