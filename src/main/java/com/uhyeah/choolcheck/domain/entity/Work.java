package com.uhyeah.choolcheck.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@Entity
public class Work{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hours_id")
    private Hours hours;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    @Builder
    public Work(Employee employee, Hours hours, LocalDate date, LocalTime startTime, LocalTime endTime) {
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
