package com.uhyeah.choolcheck.web.content.schedule;

import com.uhyeah.choolcheck.domain.entity.Employee;
import com.uhyeah.choolcheck.domain.entity.Hours;
import com.uhyeah.choolcheck.domain.entity.Schedule;
import com.uhyeah.choolcheck.domain.entity.User;
import com.uhyeah.choolcheck.domain.repository.EmployeeRepository;
import com.uhyeah.choolcheck.domain.repository.HoursRepository;
import com.uhyeah.choolcheck.domain.repository.ScheduleRepository;
import com.uhyeah.choolcheck.web.content.schedule.dto.ScheduleResponseDto;
import com.uhyeah.choolcheck.web.content.schedule.dto.ScheduleSaveRequestDto;
import com.uhyeah.choolcheck.web.content.schedule.dto.ScheduleUpdateRequestDto;
import com.uhyeah.choolcheck.global.exception.CustomException;
import com.uhyeah.choolcheck.global.exception.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final EmployeeRepository employeeRepository;
    private final HoursRepository hoursRepository;

    @Transactional
    public void save(ScheduleSaveRequestDto requestDto) {

        Employee employee = employeeRepository.findById(requestDto.getEmployeeId())
                .orElseThrow(() -> CustomException.builder()
                        .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                        .message("존재하지 않는 직원입니다.")
                        .fieldName("employeeId")
                        .rejectValue(requestDto.getEmployeeId().toString())
                        .build());

        Hours hours = getHours(requestDto.getHoursId());

        scheduleRepository.save(requestDto.toEntity(employee, hours));

    }

    @Transactional
    public void update(Long id, ScheduleUpdateRequestDto requestDto) {

        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> CustomException.builder()
                        .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                        .message("존재하지 않는 스케줄입니다.")
                        .fieldName("id")
                        .rejectValue(id.toString())
                        .build());

        Employee employee = schedule.getEmployee();
        if (!requestDto.getEmployeeId().equals(employee.getId())) {
            employee = employeeRepository.findById(requestDto.getEmployeeId())
                    .orElseThrow(() -> CustomException.builder()
                            .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                            .message("존재하지 않는 직원입니다.")
                            .fieldName("employeeId")
                            .rejectValue(requestDto.getEmployeeId().toString())
                            .build());
        }

        Hours hours = getHours(requestDto.getHoursId());
        
        schedule.update(employee, hours, requestDto.getDate(), requestDto.getStartTime(), requestDto.getEndTime());
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
    public Page<ScheduleResponseDto> getScheduleList(User loginUser, Long employeeId, LocalDate dateFrom, LocalDate dateTo, Pageable pageable) {

        return scheduleRepository.findByUserAndPeriodAndEmployee(loginUser, dateFrom, dateTo, employeeId, pageable)
                .map(ScheduleResponseDto::new);
    }


    @Transactional(readOnly = true)
    public List<List<ScheduleResponseDto>> getScheduleByWeek(User loginUser) {

        final LocalDate now = LocalDate.now();
        final LocalDate start = now.with(DayOfWeek.MONDAY);
        final LocalDate end = now.with(DayOfWeek.SUNDAY);

        List<Schedule> scheduleList = scheduleRepository.findByDateBetween(loginUser, start, end);

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
    public List<ScheduleResponseDto> getScheduleCalendar(LocalDate date, User loginUser) {

        final LocalDate start = LocalDate.now();
        final LocalDate end = date.withDayOfMonth(date.lengthOfMonth());

        return scheduleRepository.findByDateBetween(loginUser, start, end).stream()
                .map(ScheduleResponseDto::new)
                .collect(Collectors.toList());
    }


    private Hours getHours(Long hoursId) {

        if (hoursId != null) {
            return hoursRepository.findById(hoursId)
                    .orElseThrow(() -> CustomException.builder()
                            .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                            .message("존재하지 않는 근무형태입니다.")
                            .fieldName("hoursId")
                            .rejectValue(hoursId.toString())
                            .build());
        }
        return null;
    }
}
