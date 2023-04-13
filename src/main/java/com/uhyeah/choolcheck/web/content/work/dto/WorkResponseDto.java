package com.uhyeah.choolcheck.web.content.work.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import com.uhyeah.choolcheck.domain.entity.Employee;
import com.uhyeah.choolcheck.domain.entity.Hours;
import com.uhyeah.choolcheck.domain.entity.Work;
import com.uhyeah.choolcheck.domain.enums.Color;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class WorkResponseDto {

    private final Long id;

    private final String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate date;

    private final LocalTime startTime;

    private final LocalTime endTime;

    private final String hours;

    private final Color color;


    public WorkResponseDto(Work work) {
        this.id = work.getId();
        this.name = getEmployeeName(work.getEmployee());
        this.date = work.getDate();
        this.startTime = work.getStartTime();
        this.endTime = work.getEndTime();
        this.hours = getHoursTitle(work.getHours());
        this.color = work.getEmployee().getColor();
    }


    @QueryProjection
    public WorkResponseDto(Long id, String name, LocalDate date, LocalTime startTime, LocalTime endTime, String hours, Color color) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.hours = hours;
        this.color = color;
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
