package com.uhyeah.choolcheck.domain.repository;

import com.uhyeah.choolcheck.domain.entity.User;
import com.uhyeah.choolcheck.domain.entity.Work;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface WorkRepository extends JpaRepository<Work, Long>, QueryDSLRepository<Page<Work>> {

    @Query("SELECT w FROM Work w JOIN FETCH w.employee e WHERE e.user = :user AND w.date BETWEEN :start AND :end")
    List<Work> findByDateBetween(@Param("user") User user, @Param("start") LocalDate start, @Param("end") LocalDate end);

}
