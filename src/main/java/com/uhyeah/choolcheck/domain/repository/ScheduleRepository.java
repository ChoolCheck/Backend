package com.uhyeah.choolcheck.domain.repository;

import com.uhyeah.choolcheck.domain.entity.Employee;
import com.uhyeah.choolcheck.domain.entity.Schedule;
import com.uhyeah.choolcheck.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT s FROM Schedule s JOIN FETCH s.employee e WHERE e.user = :user")
    List<Schedule> findByUser(@Param("user") User user);

    List<Schedule> findByEmployee(Employee employee);

    @Query("SELECT s FROM Schedule s JOIN FETCH s.employee e WHERE e.user = :user AND s.date BETWEEN :start AND :end")
    List<Schedule> findByWeek(@Param("user") User user, @Param("start") LocalDate start, @Param("end") LocalDate end);
}
