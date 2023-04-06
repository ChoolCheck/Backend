package com.uhyeah.choolcheck.domain.repository;

import com.uhyeah.choolcheck.domain.entity.Hours;
import com.uhyeah.choolcheck.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HoursRepository extends JpaRepository<Hours, Long> {

    boolean existsByTitleAndUser(String title, User user);

    List<Hours> findByUser(User user);

}
