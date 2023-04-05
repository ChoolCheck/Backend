package com.uhyeah.choolcheck.web.statistics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uhyeah.choolcheck.domain.entity.Employee;
import com.uhyeah.choolcheck.domain.entity.Work;
import com.uhyeah.choolcheck.domain.repository.EmployeeRepository;
import com.uhyeah.choolcheck.domain.repository.WorkRepository;
import com.uhyeah.choolcheck.web.user.CustomUserDetails;
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
    public List<StatisticsResponseDto> getStatistics(LocalDate dateFrom, LocalDate dateTo, CustomUserDetails customUserDetails) {

        List<Employee> employeeList = employeeRepository.findByUser(customUserDetails.getUser());

        List<Work> workList = workRepository.findByDateBetween(customUserDetails.getUser(), dateFrom, dateTo);
        List<StatisticsResponseDto> statisticsResponseDtoList = new ArrayList<>();

        for (Employee employee : employeeList) {

            long totalTime = workList.stream()
                    .filter(work -> work.getEmployee().equals(employee))
                    .mapToLong(work -> calculateTime(work.getStartTime(), work.getEndTime()))
                    .sum();

            statisticsResponseDtoList.add(StatisticsResponseDto.builder()
                    .name(employee.getName())
                    .color(employee.getColor())
                    .totalTime(totalTime / (double) 60)
                    .build());
        }
        return statisticsResponseDtoList;
    }

    public long calculateTime(LocalTime startTime, LocalTime endTime) {

//        if (endTime.isAfter(LocalTime.MIDNIGHT)) {
//            return Duration.between(startTime, LocalTime.MIDNIGHT).toMinutes() + Duration.between()
//        }
        return Duration.between(startTime, endTime).toMinutes();
    }
}
