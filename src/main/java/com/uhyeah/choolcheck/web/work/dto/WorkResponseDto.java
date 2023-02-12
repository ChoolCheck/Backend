package com.uhyeah.choolcheck.web.work.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uhyeah.choolcheck.domain.entity.Schedule;
import com.uhyeah.choolcheck.domain.entity.Work;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class WorkResponseDto {

    private Long employee_id;

    private Long hours_id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;


    public WorkResponseDto(Work work) {
        this.employee_id = work.getEmployee().getId();
        this.hours_id = work.getHours().getId();
        this.date = work.getDate();
        this.startTime = work.getStartTime();
        this.endTime = work.getEndTime();
    }
}
