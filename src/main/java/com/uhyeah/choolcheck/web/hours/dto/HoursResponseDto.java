package com.uhyeah.choolcheck.web.hours.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.uhyeah.choolcheck.domain.entity.Hours;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class HoursResponseDto {

    private Long id;

    private String title;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @Builder
    public HoursResponseDto(Hours hours) {
        this.id = hours.getId();
        this.title = hours.getTitle();
        this.startTime = hours.getStartTime();
        this.endTime = hours.getEndTime();
    }
}
