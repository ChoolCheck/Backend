package com.uhyeah.choolcheck.web.content.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uhyeah.choolcheck.domain.entity.Employee;
import com.uhyeah.choolcheck.domain.entity.Hours;
import com.uhyeah.choolcheck.domain.entity.Schedule;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class ScheduleSaveRequestDto {

    @NotNull(message = "직원id는 필수항목입니다.")
    private Long employeeId;

    private Long hoursId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    public Schedule toEntity(Employee employee, Hours hours) {
        return Schedule.builder()
                .employee(employee)
                .hours(hours)
                .date(date)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }
}
