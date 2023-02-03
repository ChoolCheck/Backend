package com.uhyeah.choolcheck.web.user.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class UserPasswordUpdateRequestDto {

    @NotBlank(message = "비밀번호는 필수항목입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$", message = "비밀번호는 최소 8자리 이상, 최소 하나의 문자, 숫자, 특수문자를 입력해주세요.")
    private String password;


}
