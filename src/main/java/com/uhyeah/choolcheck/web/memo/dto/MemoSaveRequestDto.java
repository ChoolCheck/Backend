package com.uhyeah.choolcheck.web.memo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uhyeah.choolcheck.domain.entity.Memo;
import com.uhyeah.choolcheck.domain.entity.User;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
public class MemoSaveRequestDto {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotBlank(message = "내용은 필수항목입니다.")
    private String content;

    public Memo toEntity(User user) {
        return Memo.builder()
                .user(user)
                .date(date)
                .content(content)
                .build();
    }
}
