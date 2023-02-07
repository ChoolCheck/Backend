package com.uhyeah.choolcheck.web.memo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uhyeah.choolcheck.domain.entity.Memo;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class MemoResponseDto {

    private Long id;

    private LocalDate date; //필요한가?

    private String content;

    @Builder
    public MemoResponseDto(Memo memo) {
        this.id = memo.getId();
        this.date = memo.getDate();
        this.content = memo.getContent();
    }
}
