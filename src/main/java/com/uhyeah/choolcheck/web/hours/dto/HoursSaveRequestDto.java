package com.uhyeah.choolcheck.web.hours.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uhyeah.choolcheck.domain.entity.Hours;
import com.uhyeah.choolcheck.domain.entity.User;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
public class HoursSaveRequestDto {

    @NotBlank(message = "이름은 필수항목입니다.")
    private String title;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private Date startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private Date endTime;

    public Hours toEntity(User user) {
        return Hours.builder()
                .user(user)
                .title(title)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }
}
