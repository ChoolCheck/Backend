package com.uhyeah.choolcheck.web.statistics;

import com.uhyeah.choolcheck.domain.entity.Employee;
import com.uhyeah.choolcheck.domain.entity.Work;
import com.uhyeah.choolcheck.domain.enums.Color;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.util.Map;

@Getter
public class StatisticsResponseDto {

    private String name;

    private Color color;

    private double totalTime;

    @Builder
    public StatisticsResponseDto(String name, Color color, double totalTime) {
        this.name = name;
        this.color = color;
        this.totalTime = totalTime;
    }

    
}
