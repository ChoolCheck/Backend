package com.uhyeah.choolcheck.domain.repository;

import com.uhyeah.choolcheck.domain.entity.Employee;
import com.uhyeah.choolcheck.domain.entity.Hours;
import com.uhyeah.choolcheck.domain.entity.Schedule;
import com.uhyeah.choolcheck.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>, QueryDSLRepository<Schedule> {

    @Query("SELECT s FROM Schedule s JOIN FETCH s.employee e WHERE e.user = :user AND s.date BETWEEN :start AND :end")
    List<Schedule> findByDateBetween(@Param("user") User user, @Param("start") LocalDate start, @Param("end") LocalDate end);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Schedule s SET s.hours = null WHERE s.hours= :hours")
    void setHoursNull(@Param("hours") Hours hours);
}
