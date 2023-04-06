package com.uhyeah.choolcheck.web.hours.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uhyeah.choolcheck.domain.entity.Hours;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class HoursResponseDto {

    private final Long id;

    private final String title;

    @JsonFormat(pattern = "HH:mm")
    private final LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    private final LocalTime endTime;

    public HoursResponseDto(Hours hours) {
        this.id = hours.getId();
        this.title = hours.getTitle();
        this.startTime = hours.getStartTime();
        this.endTime = hours.getEndTime();
    }
}
