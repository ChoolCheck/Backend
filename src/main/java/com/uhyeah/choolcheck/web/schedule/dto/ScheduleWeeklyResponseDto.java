package com.uhyeah.choolcheck.web.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class ScheduleWeeklyResponseDto {

    private String day;

    @JsonFormat(pattern = "MM/dd")
    private LocalDate date;

    private List<ScheduleWeekly> schedule;

    @Builder
    public ScheduleWeeklyResponseDto(String day, LocalDate date, List<ScheduleWeekly> schedule) {
        this.day = day;
        this.date = date;
        this.schedule = schedule;
    }
}
