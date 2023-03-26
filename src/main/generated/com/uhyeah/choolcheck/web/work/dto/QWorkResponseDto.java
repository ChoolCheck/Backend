package com.uhyeah.choolcheck.web.work.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.Generated;

/**
 * com.uhyeah.choolcheck.web.work.dto.QWorkResponseDto is a Querydsl Projection type for WorkResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QWorkResponseDto extends ConstructorExpression<WorkResponseDto> {

    private static final long serialVersionUID = 1460710317L;

    public QWorkResponseDto(com.querydsl.core.types.Expression<? extends com.uhyeah.choolcheck.domain.entity.Work> work) {
        super(WorkResponseDto.class, new Class<?>[]{com.uhyeah.choolcheck.domain.entity.Work.class}, work);
    }

}

