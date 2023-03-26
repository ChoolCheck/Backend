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
import com.uhyeah.choolcheck.web.schedule.dto.ScheduleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
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

        Hours hours = null;
        if (scheduleSaveRequestDto.getHours_id() != null) {
            hours = hoursRepository.findById(scheduleSaveRequestDto.getHours_id())
                    .orElseThrow(() -> CustomException.builder()
                            .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                            .message("존재하지 않는 근무형태입니다.")
                            .fieldName("hours_id")
                            .rejectValue(scheduleSaveRequestDto.getHours_id().toString())
                            .build());
        }

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
        Hours hours = null;
        System.out.println(hours);
        System.out.println(scheduleUpdateRequestDto.getHours_id());

        if (!scheduleUpdateRequestDto.getEmployee_id().equals(employee.getId())) {
            employee = employeeRepository.findById(scheduleUpdateRequestDto.getEmployee_id())
                    .orElseThrow(() -> CustomException.builder()
                            .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                            .message("존재하지 않는 직원입니다.")
                            .fieldName("employee_id")
                            .rejectValue(scheduleUpdateRequestDto.getEmployee_id().toString())
                            .build());
        }

        if (scheduleUpdateRequestDto.getHours_id() != null) {
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
    public List<List<ScheduleResponseDto>> getScheduleByWeek(CustomUserDetails customUserDetails) {

        LocalDate now = LocalDate.now();
        LocalDate start = now.with(DayOfWeek.MONDAY);
        LocalDate end = now.with(DayOfWeek.SUNDAY);

        List<Schedule> scheduleList = scheduleRepository.findByDateBetween(customUserDetails.getUser(), start, end);
        List<List<ScheduleResponseDto>> scheduleResponseDtoList  = new ArrayList<>();
        for (int i=0; i<7; i++) {
            LocalDate date = start.plusDays(i);
            scheduleResponseDtoList.add(scheduleList.stream()
                    .filter(schedule -> schedule.getDate().isEqual(date))
                    .map(ScheduleResponseDto::new)
                    .collect(Collectors.toList()));
        }

        return scheduleResponseDtoList;
    }

    @Transactional(readOnly = true)
    public Page<ScheduleResponseDto> getScheduleList(CustomUserDetails customUserDetails, Long employeeId, String period, Pageable pageable) {

        return scheduleRepository.findByUserAndPeriodAndEmployee(customUserDetails.getUser(), period, employeeId, pageable)
                .map(ScheduleResponseDto::new);
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponseDto> getScheduleCalendar(LocalDate date, CustomUserDetails customUserDetails) {

        LocalDate start = date.withDayOfMonth(1);
        LocalDate end = date.withDayOfMonth(date.lengthOfMonth());

        return scheduleRepository.findByDateBetween(customUserDetails.getUser(), start, end).stream()
                .map(ScheduleResponseDto::new)
                .collect(Collectors.toList());
    }
}
