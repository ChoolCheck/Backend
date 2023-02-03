package com.uhyeah.choolcheck.web.employee;

import com.uhyeah.choolcheck.domain.entity.Employee;
import com.uhyeah.choolcheck.domain.repository.EmployeeRepository;
import com.uhyeah.choolcheck.web.employee.dto.EmployeeResponseDto;
import com.uhyeah.choolcheck.web.employee.dto.EmployeeSaveRequestDto;
import com.uhyeah.choolcheck.web.employee.dto.EmployeeUpdateRequestDto;
import com.uhyeah.choolcheck.web.exception.CustomException;
import com.uhyeah.choolcheck.web.exception.StatusCode;
import com.uhyeah.choolcheck.web.user.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Transactional
    public void save(EmployeeSaveRequestDto employeeSaveRequestDto, CustomUserDetails customUserDetails) {

        employeeRepository.save(employeeSaveRequestDto.toEntity(customUserDetails.getUser()));
    }

    @Transactional
    public void delete(Long id) {

        Employee employee = employeeRepository.findById(id)
                        .orElseThrow(() -> new CustomException(StatusCode.RESOURCE_NOT_FOUND, "[Employee] id: "+id));
        employeeRepository.delete(employee);
    }

    @Transactional
    public void update(Long id, EmployeeUpdateRequestDto employeeUpdateReqestDto) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new CustomException(StatusCode.RESOURCE_NOT_FOUND, "[Employee] id: "+id));

        employee.update(employeeUpdateReqestDto.getName(), employeeUpdateReqestDto.getRole(), employeeUpdateReqestDto.getColor());
    }

    @Transactional(readOnly = true)
    public EmployeeResponseDto getEmployee(Long id) {

        return employeeRepository.findById(id)
                .map(EmployeeResponseDto::new)
                .orElseThrow(() -> new CustomException(StatusCode.RESOURCE_NOT_FOUND, "[Employee] id: "+id));
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> getEmployeeList(CustomUserDetails customUserDetails) {

        return employeeRepository.findByUser(customUserDetails.getUser()).stream()
                .map(EmployeeResponseDto::new)
                .collect(Collectors.toList());
    }

}
