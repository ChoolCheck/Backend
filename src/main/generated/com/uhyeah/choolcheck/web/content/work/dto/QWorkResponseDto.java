package com.uhyeah.choolcheck.web.content.work.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.Generated;

/**
 * com.uhyeah.choolcheck.web.content.work.dto.QWorkResponseDto is a Querydsl Projection type for WorkResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QWorkResponseDto extends ConstructorExpression<WorkResponseDto> {

    private static final long serialVersionUID = -795661438L;

    public QWorkResponseDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<java.time.LocalDate> date, com.querydsl.core.types.Expression<java.time.LocalTime> startTime, com.querydsl.core.types.Expression<java.time.LocalTime> endTime, com.querydsl.core.types.Expression<String> hours, com.querydsl.core.types.Expression<com.uhyeah.choolcheck.domain.enums.Color> color) {
        super(WorkResponseDto.class, new Class<?>[]{long.class, String.class, java.time.LocalDate.class, java.time.LocalTime.class, java.time.LocalTime.class, String.class, com.uhyeah.choolcheck.domain.enums.Color.class}, id, name, date, startTime, endTime, hours, color);
    }

}

