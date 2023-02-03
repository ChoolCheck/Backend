package com.uhyeah.choolcheck.web.schedule.dto;

import com.uhyeah.choolcheck.domain.entity.Employee;
import com.uhyeah.choolcheck.domain.entity.Hours;
import com.uhyeah.choolcheck.domain.entity.Schedule;
import lombok.Getter;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
public class ScheduleSaveRequestDto {

    private Long employee_id;

    @NotBlank(message = "날짜는 필수항목입니다.")
    private Date date;

    @NotBlank(message = "시작시간은 필수항목입니다.")
    private Date startTime;

    @NotBlank(message = "종료시간은 필수항목입니다.")
    private Date endTime;

    private Hours hours;

    public Schedule toEntity(Employee employee) {
        return Schedule.builder()
                .employee(employee)
                .date(date)
                .startTime(startTime)
                .endTime(endTime)
                .hours(hours)
                .build();
    }
}
