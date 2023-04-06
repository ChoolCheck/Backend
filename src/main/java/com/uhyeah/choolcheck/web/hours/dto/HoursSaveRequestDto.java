package com.uhyeah.choolcheck.web.hours.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uhyeah.choolcheck.domain.entity.Hours;
import com.uhyeah.choolcheck.domain.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class HoursSaveRequestDto {

    @NotBlank(message = "근무형태명은 필수항목입니다.")
    private String title;

    @NotNull(message = "시작시간은 필수항목입니다.")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @NotNull(message = "종료시간은 필수항목입니다.")
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
