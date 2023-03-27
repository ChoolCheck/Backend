package com.uhyeah.choolcheck.web.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class EmailValidateRequestDto {

    @NotBlank(message = "이메일은 필수항목입니다.")
    @Email(message = "이메일 형식이 맞지 않습니다.")
    private String email;
}
