package com.uhyeah.choolcheck.domain.repository;

import com.uhyeah.choolcheck.domain.entity.Memo;
import com.uhyeah.choolcheck.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {

    List<Memo> findByUserAndDate(User user, LocalDate date);

    boolean existsByUserAndDate(User user, LocalDate date);
}
