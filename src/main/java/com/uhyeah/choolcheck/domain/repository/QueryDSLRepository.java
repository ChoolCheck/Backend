package com.uhyeah.choolcheck.domain.repository;

import com.uhyeah.choolcheck.domain.entity.User;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;


public interface QueryDSLRepository<T> {

    T findByUserAndPeriodAndEmployee(User user, LocalDate dateFrom, LocalDate dateTo, Long employeeId, Pageable pageable);
}
