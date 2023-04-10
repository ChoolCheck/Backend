package com.uhyeah.choolcheck.web.content.work;

import com.uhyeah.choolcheck.domain.entity.Employee;
import com.uhyeah.choolcheck.domain.entity.Hours;
import com.uhyeah.choolcheck.domain.entity.User;
import com.uhyeah.choolcheck.domain.entity.Work;
import com.uhyeah.choolcheck.domain.repository.EmployeeRepository;
import com.uhyeah.choolcheck.domain.repository.HoursRepository;
import com.uhyeah.choolcheck.domain.repository.WorkRepository;
import com.uhyeah.choolcheck.web.content.work.dto.WorkResponseDto;
import com.uhyeah.choolcheck.web.content.work.dto.WorkUpdateRequestDto;
import com.uhyeah.choolcheck.global.exception.CustomException;
import com.uhyeah.choolcheck.global.exception.StatusCode;
import com.uhyeah.choolcheck.web.content.work.dto.WorkSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class WorkService {

    private final WorkRepository workRepository;
    private final EmployeeRepository employeeRepository;
    private final HoursRepository hoursRepository;


    @Transactional
    public void save(WorkSaveRequestDto requestDto) {

        Employee employee = employeeRepository.findById(requestDto.getEmployeeId())
                .orElseThrow(() -> CustomException.builder()
                        .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                        .message("존재하지 않는 직원입니다.")
                        .fieldName("employeeId")
                        .rejectValue(requestDto.getEmployeeId().toString())
                        .build());

        Hours hours = getHours(requestDto.getHoursId());

        workRepository.save(requestDto.toEntity(employee, hours));
    }


    @Transactional
    public void update(Long id, WorkUpdateRequestDto requestDto) {

        Work work = workRepository.findById(id)
                .orElseThrow(() -> CustomException.builder()
                        .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                        .message("존재하지 않는 출근부입니다.")
                        .fieldName("id")
                        .rejectValue(id.toString())
                        .build());

        Employee employee = work.getEmployee();
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

        work.update(employee, hours, requestDto.getDate(), requestDto.getStartTime(), requestDto.getEndTime());
    }

    @Transactional
    public void delete(Long id) {

        Work work = workRepository.findById(id)
                .orElseThrow(() -> CustomException.builder()
                        .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                        .message("존재하지 않는 출근부입니다.")
                        .fieldName("id")
                        .rejectValue(id.toString())
                        .build());

        workRepository.delete(work);

    }


    @Transactional(readOnly = true)
    public WorkResponseDto getWork(Long id) {

        Work work = workRepository.findById(id)
                .orElseThrow(() -> CustomException.builder()
                        .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                        .message("존재하지 않는 출근부입니다.")
                        .fieldName("id")
                        .rejectValue(id.toString())
                        .build());

        return new WorkResponseDto(work);
    }


    @Transactional(readOnly = true)
    public Page<WorkResponseDto> getWorkList(User loginUser, Long employeeId, LocalDate dateFrom, LocalDate dateTo, Pageable pageable) {

        return workRepository.findByUserAndPeriodAndEmployee(loginUser, dateFrom, dateTo, employeeId, pageable)
                .map(WorkResponseDto::new);
    }


    @Transactional(readOnly = true)
    public List<WorkResponseDto> getWorkCalendar(LocalDate date, User loginUser) {

        final LocalDate start = date.withDayOfMonth(1);
        final LocalDate end = LocalDate.now().minusDays(1);

        return workRepository.findByDateBetween(loginUser, start, end).stream()
                .map(WorkResponseDto::new)
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

