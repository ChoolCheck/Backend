package com.uhyeah.choolcheck.web.user.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class UserLoginRequestDto {

    @NotBlank(message = "이메일은 필수항목입니다.")
    @Email(message = "이메일 형식이 맞지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수항목입니다.")
    private String password;

    @Builder
    public UserLoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
