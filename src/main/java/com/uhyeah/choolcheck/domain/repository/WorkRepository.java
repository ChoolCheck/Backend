package com.uhyeah.choolcheck.domain.repository;

import com.uhyeah.choolcheck.domain.entity.Employee;
import com.uhyeah.choolcheck.domain.entity.Schedule;
import com.uhyeah.choolcheck.domain.entity.User;
import com.uhyeah.choolcheck.domain.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface WorkRepository extends JpaRepository<Work, Long> {

    @Query("SELECT w FROM Work w JOIN FETCH w.employee e WHERE e.user = :user")
    List<Work> findByUser(@Param("user") User user);

    List<Work> findByEmployee(Employee employee);

    @Query("SELECT w FROM Work w JOIN FETCH w.employee e WHERE e.user = :user AND w.date BETWEEN :start AND :end")
    List<Work> findByDateBetween(@Param("user") User user, @Param("start") LocalDate start, @Param("end") LocalDate end);

//    @Query("SELECT w.employee, w.startTime, w.endTime FROM Work w JOIN FETCH w.employee e WHERE e.user = :user AND w.date BETWEEN :start AND :end")
//    List<Work> findStatisticsByDateBetween(@Param("user") User user, @Param("start") LocalDate start, @Param("end") LocalDate end);
}
