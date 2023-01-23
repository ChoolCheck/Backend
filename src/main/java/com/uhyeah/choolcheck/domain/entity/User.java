package com.uhyeah.choolcheck.domain.entity;

import com.uhyeah.choolcheck.domain.enums.Authority;
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

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder
    public User(String email, String password, String storeName, Authority authority) {
        this.email = email;
        this.password = password;
        this.storeName = storeName;
        this.authority = authority;
    }
}
