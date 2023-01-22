package com.uhyeah.choolcheck.domain.entity;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class User extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String storeName;

    @Builder
    public User(String email, String password, String storeName) {
        this.email = email;
        this.password = password;
        this.storeName = storeName;
    }
}
