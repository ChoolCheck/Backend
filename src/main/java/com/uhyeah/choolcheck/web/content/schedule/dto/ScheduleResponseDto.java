package com.uhyeah.choolcheck.web.content.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import com.uhyeah.choolcheck.domain.entity.Employee;
import com.uhyeah.choolcheck.domain.entity.Hours;
import com.uhyeah.choolcheck.domain.entity.Schedule;
import com.uhyeah.choolcheck.domain.enums.Color;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class ScheduleResponseDto {

    private final Long id;

    private final String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate date;

    private final LocalTime startTime;

    private final LocalTime endTime;

    private final String hours;

    private final Color color;

    @QueryProjection
    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.name = getEmployeeName(schedule.getEmployee());
        this.date = schedule.getDate();
        this.startTime = schedule.getStartTime();
        this.endTime = schedule.getEndTime();
        this.hours = getHoursTitle(schedule.getHours());
        this.color = schedule.getEmployee().getColor();
    }


    private String getEmployeeName(Employee employee) {

        String name = employee.getName();

        if (employee.isDelFlag()) {
            name += "(X)";
        }
        return name;
    }


    private String getHoursTitle(Hours hours) {

        if (hours != null) {
            return hours.getTitle();
        }
        return null;
    }
}
