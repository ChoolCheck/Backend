package com.uhyeah.choolcheck.web.content.schedule;

import com.uhyeah.choolcheck.domain.entity.Employee;
import com.uhyeah.choolcheck.domain.entity.Hours;
import com.uhyeah.choolcheck.domain.entity.Schedule;
import com.uhyeah.choolcheck.domain.repository.EmployeeRepository;
import com.uhyeah.choolcheck.domain.repository.HoursRepository;
import com.uhyeah.choolcheck.domain.repository.ScheduleRepository;
import com.uhyeah.choolcheck.web.content.schedule.dto.ScheduleResponseDto;
import com.uhyeah.choolcheck.web.content.schedule.dto.ScheduleSaveRequestDto;
import com.uhyeah.choolcheck.web.content.schedule.dto.ScheduleUpdateRequestDto;
import com.uhyeah.choolcheck.global.exception.CustomException;
import com.uhyeah.choolcheck.global.exception.StatusCode;
import com.uhyeah.choolcheck.web.user.CustomUserDetails;
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
    public void save(ScheduleSaveRequestDto scheduleSaveRequestDto) {

        Employee employee = employeeRepository.findById(scheduleSaveRequestDto.getEmployeeId())
                .orElseThrow(() -> CustomException.builder()
                        .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                        .message("존재하지 않는 직원입니다.")
                        .fieldName("employeeId")
                        .rejectValue(scheduleSaveRequestDto.getEmployeeId().toString())
                        .build());

        Hours hours = null;
        if (scheduleSaveRequestDto.getHoursId() != null) {
            hours = hoursRepository.findById(scheduleSaveRequestDto.getHoursId())
                    .orElseThrow(() -> CustomException.builder()
                            .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                            .message("존재하지 않는 근무형태입니다.")
                            .fieldName("hoursId")
                            .rejectValue(scheduleSaveRequestDto.getHoursId().toString())
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
        System.out.println(scheduleUpdateRequestDto.getHoursId());

        if (!scheduleUpdateRequestDto.getEmployeeId().equals(employee.getId())) {
            employee = employeeRepository.findById(scheduleUpdateRequestDto.getEmployeeId())
                    .orElseThrow(() -> CustomException.builder()
                            .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                            .message("존재하지 않는 직원입니다.")
                            .fieldName("employeeId")
                            .rejectValue(scheduleUpdateRequestDto.getEmployeeId().toString())
                            .build());
        }

        if (scheduleUpdateRequestDto.getHoursId() != null) {
            hours = hoursRepository.findById(scheduleUpdateRequestDto.getHoursId())
                    .orElseThrow(() -> CustomException.builder()
                            .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                            .message("존재하지 않는 근무형태입니다.")
                            .fieldName("hoursId")
                            .rejectValue(scheduleUpdateRequestDto.getHoursId().toString())
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
    public Page<ScheduleResponseDto> getScheduleList(CustomUserDetails customUserDetails, Long employeeId, LocalDate dateFrom, LocalDate dateTo, Pageable pageable) {

        return scheduleRepository.findByUserAndPeriodAndEmployee(customUserDetails.getUser(), dateFrom, dateTo, employeeId, pageable)
                .map(ScheduleResponseDto::new);
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponseDto> getScheduleCalendar(LocalDate date, CustomUserDetails customUserDetails) {

        LocalDate start = LocalDate.now();
        LocalDate end = date.withDayOfMonth(date.lengthOfMonth());

        return scheduleRepository.findByDateBetween(customUserDetails.getUser(), start, end).stream()
                .map(ScheduleResponseDto::new)
                .collect(Collectors.toList());
    }
}
