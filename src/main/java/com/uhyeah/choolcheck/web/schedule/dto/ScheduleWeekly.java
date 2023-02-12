package com.uhyeah.choolcheck.web.schedule.dto;

import com.uhyeah.choolcheck.domain.entity.Schedule;
import lombok.Getter;

@Getter
public class ScheduleWeekly {

    private String name;

    private String time;

    private String backgroundColor;

    public ScheduleWeekly(Schedule schedule) {
        this.name = schedule.getEmployee().getName();
        this.time = schedule.getStartTime().toString() + "-" + schedule.getEndTime().toString();
        this.backgroundColor = schedule.getEmployee().getColor().getCode();
    }
}
