package com.uhyeah.choolcheck.web.work;

import com.uhyeah.choolcheck.domain.entity.Employee;
import com.uhyeah.choolcheck.domain.entity.Hours;
import com.uhyeah.choolcheck.domain.entity.Work;
import com.uhyeah.choolcheck.domain.repository.EmployeeRepository;
import com.uhyeah.choolcheck.domain.repository.HoursRepository;
import com.uhyeah.choolcheck.domain.repository.WorkRepository;
import com.uhyeah.choolcheck.web.exception.CustomException;
import com.uhyeah.choolcheck.web.exception.StatusCode;
import com.uhyeah.choolcheck.web.work.dto.WorkResponseDto;
import com.uhyeah.choolcheck.web.work.dto.WorkSaveRequestDto;
import com.uhyeah.choolcheck.web.work.dto.WorkUpdateRequestDto;
import com.uhyeah.choolcheck.web.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        Employee employee = employeeRepository.findById(workSaveRequestDto.getEmployee_id())
                .orElseThrow(() -> CustomException.builder()
                        .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                        .message("존재하지 않는 직원입니다.")
                        .fieldName("employee_id")
                        .rejectValue(workSaveRequestDto.getEmployee_id().toString())
                        .build());

        Hours hours = hoursRepository.findById(workSaveRequestDto.getHours_id())
                .orElseThrow(() -> CustomException.builder()
                        .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                        .message("존재하지 않는 근무형태입니다.")
                        .fieldName("hours_id")
                        .rejectValue(workSaveRequestDto.getHours_id().toString())
                        .build());

        workRepository.save(workSaveRequestDto.toEntity(employee, hours));

    }

    @Transactional
    public void update(Long id, WorkUpdateRequestDto workUpdateRequestDto) {

        Work work = workRepository.findById(id)
                .orElseThrow(() -> CustomException.builder()
                        .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                        .message("존재하지 않는 스케줄입니다.")
                        .fieldName("id")
                        .rejectValue(id.toString())
                        .build());

        Employee employee = work.getEmployee();
        Hours hours = work.getHours();

        if (workUpdateRequestDto.getEmployee_id().equals(employee.getId())) {
            employee = employeeRepository.findById(workUpdateRequestDto.getEmployee_id())
                    .orElseThrow(() -> CustomException.builder()
                            .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                            .message("존재하지 않는 직원입니다.")
                            .fieldName("employee_id")
                            .rejectValue(workUpdateRequestDto.getEmployee_id().toString())
                            .build());
        }

        if (workUpdateRequestDto.getHours_id().equals(hours.getId())) {
            hours = hoursRepository.findById(workUpdateRequestDto.getHours_id())
                    .orElseThrow(() -> CustomException.builder()
                            .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                            .message("존재하지 않는 근무형태입니다.")
                            .fieldName("hours_id")
                            .rejectValue(workUpdateRequestDto.getHours_id().toString())
                            .build());
        }

        work.update(employee, hours, workUpdateRequestDto.getDate(), workUpdateRequestDto.getStartTime(), workUpdateRequestDto.getEndTime());

    }

    @Transactional
    public void delete(Long id) {

        Work work = workRepository.findById(id)
                .orElseThrow(() -> CustomException.builder()
                        .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                        .message("존재하지 않는 스케줄입니다.")
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
                        .message("존재하지 않는 스케줄입니다.")
                        .fieldName("id")
                        .rejectValue(id.toString())
                        .build());

        return new WorkResponseDto(work);
    }

    @Transactional(readOnly = true)
    public List<WorkResponseDto> getWorkByEmployee(Long employee_id) {

        Employee employee = employeeRepository.findById(employee_id)
                .orElseThrow(() -> CustomException.builder()
                        .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                        .message("존재하지 않는 직원입니다.")
                        .fieldName("employee_id")
                        .rejectValue(employee_id.toString())
                        .build());

        return workRepository.findByEmployee(employee).stream()
                .map(WorkResponseDto::new)
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<WorkResponseDto> getWorkList(CustomUserDetails customUserDetails) {

        return workRepository.findByUser(customUserDetails.getUser()).stream()
                .map(WorkResponseDto::new)
                .collect(Collectors.toList());
    }
}
