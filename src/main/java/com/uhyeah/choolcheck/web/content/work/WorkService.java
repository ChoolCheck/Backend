package com.uhyeah.choolcheck.web.content.work;

import com.uhyeah.choolcheck.domain.entity.Employee;
import com.uhyeah.choolcheck.domain.entity.Hours;
import com.uhyeah.choolcheck.domain.entity.Work;
import com.uhyeah.choolcheck.domain.repository.EmployeeRepository;
import com.uhyeah.choolcheck.domain.repository.HoursRepository;
import com.uhyeah.choolcheck.domain.repository.WorkRepository;
import com.uhyeah.choolcheck.web.content.work.dto.WorkResponseDto;
import com.uhyeah.choolcheck.web.content.work.dto.WorkUpdateRequestDto;
import com.uhyeah.choolcheck.web.exception.CustomException;
import com.uhyeah.choolcheck.web.exception.StatusCode;
import com.uhyeah.choolcheck.web.content.work.dto.WorkSaveRequestDto;
import com.uhyeah.choolcheck.web.user.CustomUserDetails;
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
    public void save(WorkSaveRequestDto workSaveRequestDto) {

        Employee employee = employeeRepository.findById(workSaveRequestDto.getEmployeeId())
                .orElseThrow(() -> CustomException.builder()
                        .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                        .message("존재하지 않는 직원입니다.")
                        .fieldName("employeeId")
                        .rejectValue(workSaveRequestDto.getEmployeeId().toString())
                        .build());

        Hours hours = null;
        if (workSaveRequestDto.getHoursId() != null) {
            hours = hoursRepository.findById(workSaveRequestDto.getHoursId())
                    .orElseThrow(() -> CustomException.builder()
                            .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                            .message("존재하지 않는 근무형태입니다.")
                            .fieldName("hoursId")
                            .rejectValue(workSaveRequestDto.getHoursId().toString())
                            .build());
        }


        workRepository.save(workSaveRequestDto.toEntity(employee, hours));

    }

    @Transactional
    public void update(Long id, WorkUpdateRequestDto workUpdateRequestDto) {

        Work work = workRepository.findById(id)
                .orElseThrow(() -> CustomException.builder()
                        .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                        .message("존재하지 않는 출근부입니다.")
                        .fieldName("id")
                        .rejectValue(id.toString())
                        .build());

        Employee employee = work.getEmployee();
        Hours hours = work.getHours();

        if (!workUpdateRequestDto.getEmployeeId().equals(employee.getId())) {
            employee = employeeRepository.findById(workUpdateRequestDto.getEmployeeId())
                    .orElseThrow(() -> CustomException.builder()
                            .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                            .message("존재하지 않는 직원입니다.")
                            .fieldName("employeeId")
                            .rejectValue(workUpdateRequestDto.getEmployeeId().toString())
                            .build());
        }

        if (workUpdateRequestDto.getHoursId() != null) {
            hours = hoursRepository.findById(workUpdateRequestDto.getHoursId())
                    .orElseThrow(() -> CustomException.builder()
                            .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                            .message("존재하지 않는 근무형태입니다.")
                            .fieldName("hoursId")
                            .rejectValue(workUpdateRequestDto.getHoursId().toString())
                            .build());
        }
        work.update(employee, hours, workUpdateRequestDto.getDate(), workUpdateRequestDto.getStartTime(), workUpdateRequestDto.getEndTime());

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
    public Page<WorkResponseDto> getWorkList(CustomUserDetails customUserDetails, Long employeeId, LocalDate dateFrom, LocalDate dateTo, Pageable pageable) {

        return workRepository.findByUserAndPeriodAndEmployee(customUserDetails.getUser(), dateFrom, dateTo, employeeId, pageable)
                .map(WorkResponseDto::new);
    }

    @Transactional(readOnly = true)
    public List<WorkResponseDto> getWorkCalendar(LocalDate date, CustomUserDetails customUserDetails) {

        LocalDate start = date.withDayOfMonth(1);
        LocalDate end = LocalDate.now().minusDays(1);

        return workRepository.findByDateBetween(customUserDetails.getUser(), start, end).stream()
                .map(WorkResponseDto::new)
                .collect(Collectors.toList());
    }

}

