package com.uhyeah.choolcheck.web.memo.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class MemoCalendarResponseDto {

    final private LocalDate date;

    final private boolean exist;
}
