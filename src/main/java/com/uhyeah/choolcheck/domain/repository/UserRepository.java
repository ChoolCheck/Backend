package com.uhyeah.choolcheck.domain.repository;

import com.uhyeah.choolcheck.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
}
