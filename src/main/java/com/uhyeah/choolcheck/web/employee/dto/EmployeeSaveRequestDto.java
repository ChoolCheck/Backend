package com.uhyeah.choolcheck.web.employee.dto;

import com.uhyeah.choolcheck.domain.entity.Employee;
import com.uhyeah.choolcheck.domain.entity.User;
import com.uhyeah.choolcheck.domain.enums.Color;
import com.uhyeah.choolcheck.domain.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class EmployeeSaveRequestDto {

    @NotBlank(message = "이름은 필수항목입니다.")
    private String name;

    @NotNull(message = "역할은 필수항목입니다.")
    private Role role;

    @NotNull(message = "색상은 필수항목입니다.")
    private Color color;

    public Employee toEntity(User user) {
        return Employee.builder()
                .user(user)
                .name(name)
                .role(role)
                .color(color)
                .build();
    }
}
