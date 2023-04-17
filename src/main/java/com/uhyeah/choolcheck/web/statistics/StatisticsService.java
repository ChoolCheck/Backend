package com.uhyeah.choolcheck.web.statistics;

import com.uhyeah.choolcheck.domain.entity.Employee;
import com.uhyeah.choolcheck.domain.entity.User;
import com.uhyeah.choolcheck.domain.entity.Work;
import com.uhyeah.choolcheck.domain.repository.EmployeeRepository;
import com.uhyeah.choolcheck.domain.repository.WorkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class StatisticsService {

    private final WorkRepository workRepository;
    private final EmployeeRepository employeeRepository;


    @Transactional(readOnly = true)
    public List<StatisticsResponseDto> getStatistics(LocalDate dateFrom, LocalDate dateTo, User loginUser) {

        List<Employee> employeeList = employeeRepository.findByUser(loginUser);
        List<Work> workList = workRepository.findByDateBetween(loginUser, dateFrom, dateTo);

        List<StatisticsResponseDto> statisticsResponseDtoList = new ArrayList<>();
        for (Employee employee : employeeList) {

            double totalTime = getTotalTime(workList, employee);

            statisticsResponseDtoList.add(StatisticsResponseDto.builder()
                    .name(employee.getName())
                    .color(employee.getColor())
                    .totalTime(totalTime)
                    .build());
        }
        return statisticsResponseDtoList;
    }


    private double calculateHour(LocalTime startTime, LocalTime endTime) {

        if (endTime.isBefore(startTime)) {
            return (Duration.between(startTime, endTime).toMinutes() + (24 * 60)) / 60.0;
        }
        else {
            return Duration.between(startTime, endTime).toMinutes() / 60.0;
        }
    }


    private double getTotalTime(List<Work> workList, Employee employee) {

        return workList.stream()
                .filter(work -> work.getEmployee().equals(employee))
                .mapToDouble(work -> calculateHour(work.getStartTime(), work.getEndTime()))
                .sum();
    }
}
