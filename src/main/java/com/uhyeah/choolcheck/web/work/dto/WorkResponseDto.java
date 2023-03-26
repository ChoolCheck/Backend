package com.uhyeah.choolcheck.web.work.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import com.uhyeah.choolcheck.domain.entity.Work;
import com.uhyeah.choolcheck.domain.entity.Work;
import com.uhyeah.choolcheck.domain.enums.Color;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class WorkResponseDto {

    private Long id;

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private String hours;

    private Color color;

    @QueryProjection
    public WorkResponseDto(Work work) {
        this.id = work.getId();
        this.name = work.getEmployee().getName();
        this.date = work.getDate();
        this.startTime = work.getStartTime();
        this.endTime = work.getEndTime();
        this.color = work.getEmployee().getColor();

        if (work.getHours() != null) {
            this.hours = work.getHours().getTitle();
        }
    }


}
