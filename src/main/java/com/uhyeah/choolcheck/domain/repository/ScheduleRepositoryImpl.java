package com.uhyeah.choolcheck.domain.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.uhyeah.choolcheck.domain.entity.Schedule;
import com.uhyeah.choolcheck.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.uhyeah.choolcheck.domain.entity.QSchedule.schedule;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements QueryDSLRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Schedule> findByUserAndPeriodAndEmployee(User user, LocalDate dateFrom, LocalDate dateTo, Long employeeId, Pageable pageable) {

        List<Schedule> content = jpaQueryFactory
                .selectFrom(schedule)
                .where(userEq(user), dateBetween(dateFrom, dateTo), employeeIdEq(employeeId))
                .orderBy(schedule.date.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = jpaQueryFactory
                .select(schedule.count())
                .from(schedule)
                .where(userEq(user), dateBetween(dateFrom, dateTo), employeeIdEq(employeeId));

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    private BooleanExpression userEq(User user) {
        return schedule.employee.user.eq(user);
    }

    private BooleanExpression dateBetween(LocalDate dateFrom, LocalDate dateTo) {

        if (dateFrom == null & dateTo == null) {
            return null;
        }

        return schedule.date.between(dateFrom, dateTo);
    }

    private BooleanExpression employeeIdEq(Long employeeId) {
        if(employeeId == null) {
            return null;
        }
        return schedule.employee.id.eq(employeeId);
    }
}
