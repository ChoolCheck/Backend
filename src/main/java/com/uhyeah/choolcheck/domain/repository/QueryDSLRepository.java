package com.uhyeah.choolcheck.domain.repository;

import com.uhyeah.choolcheck.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;


public interface QueryDSLRepository<E> {

    Page<E> findByUserAndPeriodAndEmployee(User user, LocalDate dateFrom, LocalDate dateTo, Long employeeId, Pageable pageable);
}
