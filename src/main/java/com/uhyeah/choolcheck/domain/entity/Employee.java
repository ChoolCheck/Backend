package com.uhyeah.choolcheck.domain.entity;

import com.uhyeah.choolcheck.domain.enums.Color;
import com.uhyeah.choolcheck.domain.enums.Role;
import com.uhyeah.choolcheck.web.employee.dto.EmployeeUpdateRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Color color;

    @Builder
    public Employee(User user, String name, Role role, Color color) {
        this.user = user;
        this.name = name;
        this.role = role;
        this.color = color;
    }

    public void update(String name, Role role, Color color) {
        this.name = name;
        this.role = role;
        this.color = color;
    }
}
