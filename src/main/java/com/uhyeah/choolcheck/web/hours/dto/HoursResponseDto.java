package com.uhyeah.choolcheck.web.hours.dto;

import com.uhyeah.choolcheck.domain.entity.Hours;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
public class HoursResponseDto {

    private String title;

    private Date startTime;

    private Date endTime;

    @Builder
    public HoursResponseDto(Hours hours) {
        this.title = hours.getTitle();
        this.startTime = hours.getStartTime();
        this.endTime = hours.getEndTime();
    }
}
