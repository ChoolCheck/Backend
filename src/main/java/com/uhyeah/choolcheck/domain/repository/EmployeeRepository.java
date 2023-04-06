package com.uhyeah.choolcheck.domain.repository;

import com.uhyeah.choolcheck.domain.entity.Employee;
import com.uhyeah.choolcheck.domain.entity.User;
import com.uhyeah.choolcheck.domain.enums.Color;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByNameAndColorAndUserAndDelFlagFalse(String name, Color color, User user);

    List<Employee> findByUserAndDelFlagFalse(User user);

    List<Employee> findByUser(User user);
}
