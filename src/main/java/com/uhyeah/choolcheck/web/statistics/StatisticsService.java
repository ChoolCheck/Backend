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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
                    .mapToLong(work -> Duration.between(work.getStartTime(), work.getEndTime()).toMinutes())
                    .sum();

            statisticsResponseDtoList.add(StatisticsResponseDto.builder()
                    .name(employee.getName())
                    .color(employee.getColor())
                    .totalTime(totalTime / (double) 60)
                    .build());
        }
        return statisticsResponseDtoList;
    }
}
