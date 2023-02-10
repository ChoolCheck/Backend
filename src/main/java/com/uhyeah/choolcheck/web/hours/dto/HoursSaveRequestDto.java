package com.uhyeah.choolcheck.web.hours.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uhyeah.choolcheck.domain.entity.Hours;
import com.uhyeah.choolcheck.domain.entity.User;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.time.LocalTime;

@Getter
public class HoursSaveRequestDto {

    @NotBlank(message = "근무형태명은 필수항목입니다.")
    private String title;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    public Hours toEntity(User user) {
        return Hours.builder()
                .user(user)
                .title(title)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }
}
