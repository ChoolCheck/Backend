package com.uhyeah.choolcheck.web.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uhyeah.choolcheck.domain.entity.Schedule;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

@Getter
public class ScheduleResponseDto {

    private String day;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate date;

    private String name;

    private String time;

    private long workTime;

    private String backgroundColor;


    public ScheduleResponseDto(Schedule schedule) {
        this.day = schedule.getDate().getDayOfWeek().getDisplayName(TextStyle.NARROW, Locale.KOREAN);
        this.date = schedule.getDate();
        this.name = schedule.getEmployee().getName();
        this.time = schedule.getStartTime().toString() + "-" + schedule.getEndTime().toString();
        this.workTime = Duration.between(schedule.getStartTime(), schedule.getEndTime()).toHours();
        this.backgroundColor = schedule.getEmployee().getColor().getCode();
    }


}
