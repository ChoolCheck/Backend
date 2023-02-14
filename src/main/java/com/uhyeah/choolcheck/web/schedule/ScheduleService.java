package com.uhyeah.choolcheck.web.schedule;

import com.uhyeah.choolcheck.domain.entity.Employee;
import com.uhyeah.choolcheck.domain.entity.Hours;
import com.uhyeah.choolcheck.domain.entity.Schedule;
import com.uhyeah.choolcheck.domain.repository.EmployeeRepository;
import com.uhyeah.choolcheck.domain.repository.HoursRepository;
import com.uhyeah.choolcheck.domain.repository.ScheduleRepository;
import com.uhyeah.choolcheck.web.exception.CustomException;
import com.uhyeah.choolcheck.web.exception.StatusCode;
import com.uhyeah.choolcheck.web.schedule.dto.*;
import com.uhyeah.choolcheck.web.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final EmployeeRepository employeeRepository;
    private final HoursRepository hoursRepository;

    @Transactional
    public void save(ScheduleSaveRequestDto scheduleSaveRequestDto) {

        Employee employee = employeeRepository.findById(scheduleSaveRequestDto.getEmployee_id())
                .orElseThrow(() -> CustomException.builder()
                        .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                        .message("존재하지 않는 직원입니다.")
                        .fieldName("employee_id")
                        .rejectValue(scheduleSaveRequestDto.getEmployee_id().toString())
                        .build());

        Hours hours = hoursRepository.findById(scheduleSaveRequestDto.getHours_id())
                .orElseThrow(() -> CustomException.builder()
                        .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                        .message("존재하지 않는 근무형태입니다.")
                        .fieldName("hours_id")
                        .rejectValue(scheduleSaveRequestDto.getHours_id().toString())
                        .build());

        scheduleRepository.save(scheduleSaveRequestDto.toEntity(employee, hours));

    }

    @Transactional
    public void update(Long id, ScheduleUpdateRequestDto scheduleUpdateRequestDto) {

        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> CustomException.builder()
                        .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                        .message("존재하지 않는 스케줄입니다.")
                        .fieldName("id")
                        .rejectValue(id.toString())
                        .build());

        Employee employee = schedule.getEmployee();
        Hours hours = schedule.getHours();

        if (scheduleUpdateRequestDto.getEmployee_id().equals(employee.getId())) {
            employee = employeeRepository.findById(scheduleUpdateRequestDto.getEmployee_id())
                    .orElseThrow(() -> CustomException.builder()
                            .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                            .message("존재하지 않는 직원입니다.")
                            .fieldName("employee_id")
                            .rejectValue(scheduleUpdateRequestDto.getEmployee_id().toString())
                            .build());
        }

        if (scheduleUpdateRequestDto.getHours_id().equals(hours.getId())) {
            hours = hoursRepository.findById(scheduleUpdateRequestDto.getHours_id())
                    .orElseThrow(() -> CustomException.builder()
                            .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                            .message("존재하지 않는 근무형태입니다.")
                            .fieldName("hours_id")
                            .rejectValue(scheduleUpdateRequestDto.getHours_id().toString())
                            .build());
        }

        schedule.update(employee, hours, scheduleUpdateRequestDto.getDate(), scheduleUpdateRequestDto.getStartTime(), scheduleUpdateRequestDto.getEndTime());

    }

    @Transactional
    public void delete(Long id) {

        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> CustomException.builder()
                        .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                        .message("존재하지 않는 스케줄입니다.")
                        .fieldName("id")
                        .rejectValue(id.toString())
                        .build());

        scheduleRepository.delete(schedule);

    }

    @Transactional(readOnly = true)
    public ScheduleResponseDto getSchedule(Long id) {

        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> CustomException.builder()
                        .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                        .message("존재하지 않는 스케줄입니다.")
                        .fieldName("id")
                        .rejectValue(id.toString())
                        .build());

        return new ScheduleResponseDto(schedule);
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponseDto> getScheduleByEmployee(Long employee_id) {

        Employee employee = employeeRepository.findById(employee_id)
                .orElseThrow(() -> CustomException.builder()
                        .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                        .message("존재하지 않는 직원입니다.")
                        .fieldName("employee_id")
                        .rejectValue(employee_id.toString())
                        .build());

        return scheduleRepository.findByEmployee(employee).stream()
                .map(ScheduleResponseDto::new)
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<ScheduleWeeklyResponseDto> getScheduleByWeek(CustomUserDetails customUserDetails) {

        List<LocalDate> week = new ArrayList<>();
        LocalDate now = LocalDate.now();

        for (int i = DayOfWeek.MONDAY.getValue(); i<= DayOfWeek.SUNDAY.getValue(); i++) {
            week.add(now.with(DayOfWeek.of(i)));
        }

        List<Schedule> scheduleList = scheduleRepository.findByDateBetween(customUserDetails.getUser(), week.get(0), week.get(week.size()-1));

        List<ScheduleWeeklyResponseDto> scheduleWeeklyResponseDtoList = new ArrayList<>();
        for (LocalDate date : week) {

            List<ScheduleWeekly> scheduleWeeklyList = scheduleList.stream()
                    .filter(schedule -> schedule.getDate().isEqual(date))
                    .map(ScheduleWeekly::new)
                    .collect(Collectors.toList());

            scheduleWeeklyResponseDtoList.add(ScheduleWeeklyResponseDto.builder()
                            .day(date.getDayOfWeek().getDisplayName(TextStyle.NARROW, Locale.KOREAN))
                            .date(date)
                            .schedule(scheduleWeeklyList)
                            .build());
        }

        return scheduleWeeklyResponseDtoList;
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponseDto> getScheduleList(CustomUserDetails customUserDetails) {

        return scheduleRepository.findByUser(customUserDetails.getUser()).stream()
                .map(ScheduleResponseDto::new)
                .collect(Collectors.toList());
    }
}
