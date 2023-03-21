package com.uhyeah.choolcheck.web.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class ScheduleUpdateRequestDto {

    @NotNull(message = "직원id는 필수항목입니다.")
    private Long employee_id;

    private Long hours_id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

}
