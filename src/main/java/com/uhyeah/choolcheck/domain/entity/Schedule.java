package com.uhyeah.choolcheck.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@Entity
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne()
    @JoinColumn(name = "hours_id")
    private Hours hours;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;


    @Builder
    public Schedule(Employee employee, Hours hours, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.employee = employee;
        this.hours = hours;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void update(Employee employee, Hours hours, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.employee = employee;
        this.hours = hours;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
