package com.uhyeah.choolcheck.web.statistics;

import com.uhyeah.choolcheck.domain.enums.Color;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StatisticsResponseDto {

    private final String name;

    private final Color color;

    private final double totalTime;
}
