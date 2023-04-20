package com.uhyeah.choolcheck.domain.entity;

import com.uhyeah.choolcheck.domain.enums.Color;
import com.uhyeah.choolcheck.domain.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Color color;

    private boolean delFlag;

    @Builder
    public Employee(User user, String name, Role role, Color color) {
        this.user = user;
        this.name = getEmployeeName();
        this.role = role;
        this.color = color;
        this.delFlag = false;
    }

    public void update(String name, Role role, Color color) {
        this.name = name;
        this.role = role;
        this.color = color;
    }

    public void setDelFlag() {
        this.delFlag = true;
    }


//    private String getEmployeeName() {
//
//        if (delFlag) {
//            return name += "(X)";
//        }
//        return name;
//    }
}
