package com.uhyeah.choolcheck.web.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uhyeah.choolcheck.domain.entity.Hours;
import com.uhyeah.choolcheck.domain.entity.Schedule;
import com.uhyeah.choolcheck.domain.enums.Color;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class ScheduleResponseDto {

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private String hours;

    private Color color;


    public ScheduleResponseDto(Schedule schedule) {
        this.name = schedule.getEmployee().getName();
        this.date = schedule.getDate();
        this.startTime = schedule.getStartTime();
        this.endTime = schedule.getEndTime();
        this.color = schedule.getEmployee().getColor();

        if (schedule.getHours() != null) {
            this.hours = schedule.getHours().getTitle();
        }
    }
}
