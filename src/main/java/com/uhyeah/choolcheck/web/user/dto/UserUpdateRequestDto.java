package com.uhyeah.choolcheck.web.user.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class UserUpdateRequestDto {

    @NotBlank(message = "상호명은 필수항목입니다.")
    private String storeName;

}
