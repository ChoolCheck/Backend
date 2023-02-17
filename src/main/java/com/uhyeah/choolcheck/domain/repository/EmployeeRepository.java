package com.uhyeah.choolcheck.domain.repository;

import com.uhyeah.choolcheck.domain.entity.Employee;
import com.uhyeah.choolcheck.domain.entity.User;
import com.uhyeah.choolcheck.domain.enums.Color;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByNameAndColorAndUser(String name, Color color, User user);

    boolean existsByNameAndColorAndUserAndIdNot(String name, Color color, User user, Long id);

    List<Employee> findByUser(User user);
}
