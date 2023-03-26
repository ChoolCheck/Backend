package com.uhyeah.choolcheck.domain.repository;

import com.uhyeah.choolcheck.domain.entity.User;
import org.springframework.data.domain.Pageable;


public interface QueryRepository<T> {

    T findByUserAndPeriodAndEmployee(User user, String period, Long employeeId, Pageable pageable);
}
