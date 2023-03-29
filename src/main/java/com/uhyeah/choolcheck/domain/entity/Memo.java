package com.uhyeah.choolcheck.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
public class Memo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    private LocalDate date;

    private String content;

    @Builder
    public Memo(LocalDate date, String content, User user) {
        this.user = user;
        this.date = date;
        this.content = content;
    }

    public void update(LocalDate date, String content) {
        this.date = date;
        this.content = content;
    }
}
