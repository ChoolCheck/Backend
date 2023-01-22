package com.uhyeah.choolcheck.web.user.dto;

import com.uhyeah.choolcheck.domain.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class UserSaveRequestDto {

    @NotBlank(message = "이메일은 필수항목입니다.")
    @Email(message = "이메일 형식이 맞지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수항목입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$", message = "비밀번호는 최소 8자리 이상, 최소 하나의 문자, 숫자, 특수문자를 입력해주세요.")
    private String password;

    @NotBlank(message = "상호명은 필수항목입니다.")
    private String storeName;

    private String passwordEncoded;

    @Builder
    public UserSaveRequestDto(String email, String password, String storeName) {
        this.email = email;
        this.password = password;
        this.storeName = storeName;
    }

    public void setPasswordEncoded(String passwordEncoded) {
        this.passwordEncoded = passwordEncoded;
    }

    public User toEntity() {
        return User.builder()
                .email(email)
                .password(passwordEncoded)
                .storeName(storeName)
                .build();

    }
}
