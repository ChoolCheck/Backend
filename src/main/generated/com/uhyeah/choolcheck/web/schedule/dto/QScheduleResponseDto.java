package com.uhyeah.choolcheck.web.schedule.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.Generated;

/**
 * com.uhyeah.choolcheck.web.schedule.dto.QScheduleResponseDto is a Querydsl Projection type for ScheduleResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QScheduleResponseDto extends ConstructorExpression<ScheduleResponseDto> {

    private static final long serialVersionUID = -1248336915L;

    public QScheduleResponseDto(com.querydsl.core.types.Expression<? extends com.uhyeah.choolcheck.domain.entity.Schedule> schedule) {
        super(ScheduleResponseDto.class, new Class<?>[]{com.uhyeah.choolcheck.domain.entity.Schedule.class}, schedule);
    }

}

