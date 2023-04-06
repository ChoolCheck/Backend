package com.uhyeah.choolcheck.web.employee.dto;

import com.uhyeah.choolcheck.domain.entity.Employee;
import com.uhyeah.choolcheck.domain.enums.Color;
import com.uhyeah.choolcheck.domain.enums.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
public class EmployeeResponseDto {

    private final Long id;

    private final String name;

    private final Role role;

    private final Color color;

    public EmployeeResponseDto(Employee employee) {
        this.id = employee.getId();
        this.name = employee.getName();
        this.role = employee.getRole();
        this.color = employee.getColor();

    }
}
