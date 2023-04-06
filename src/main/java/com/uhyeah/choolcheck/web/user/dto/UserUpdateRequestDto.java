package com.uhyeah.choolcheck.web.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class UserUpdateRequestDto {

    @NotBlank(message = "상호명은 필수항목입니다.")
    private String storeName;
}
