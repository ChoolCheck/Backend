package com.uhyeah.choolcheck.domain.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.uhyeah.choolcheck.domain.entity.Schedule;
import com.uhyeah.choolcheck.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.uhyeah.choolcheck.domain.entity.QSchedule.schedule;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements QueryRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Schedule> findByUserAndPeriodAndEmployee(User user, String period, Long employeeId, Pageable pageable) {

        List<Schedule> content = jpaQueryFactory
                .selectFrom(schedule)
                .where(userEq(user), dateBetween(period), employeeIdEq(employeeId))
                .orderBy(schedule.date.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, content.size());
    }

    private BooleanExpression userEq(User user) {
        return schedule.employee.user.eq(user);
    }

    private BooleanExpression dateBetween(String period) {

        if (period == null) {
            return null;
        }

        String[] date = period.split(",");
        LocalDate from = LocalDate.parse(date[0]);
        LocalDate to = LocalDate.parse(date[1]);

        return schedule.date.between(from, to);
    }

    private BooleanExpression employeeIdEq(Long employeeId) {
        if(employeeId == null) {
            return null;
        }
        return schedule.employee.id.eq(employeeId);
    }
}
