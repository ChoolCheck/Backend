package com.uhyeah.choolcheck.web.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ScheduleWeeklyResponseDto {

    private String day;

    @JsonFormat(pattern = "MM/dd")
    private LocalDate date;


}
