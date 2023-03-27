package com.uhyeah.choolcheck.web.employee;

import com.uhyeah.choolcheck.domain.entity.Employee;
import com.uhyeah.choolcheck.domain.entity.User;
import com.uhyeah.choolcheck.domain.enums.Color;
import com.uhyeah.choolcheck.domain.repository.EmployeeRepository;
import com.uhyeah.choolcheck.web.employee.dto.EmployeeResponseDto;
import com.uhyeah.choolcheck.web.employee.dto.EmployeeSaveRequestDto;
import com.uhyeah.choolcheck.web.employee.dto.EmployeeUpdateRequestDto;
import com.uhyeah.choolcheck.global.exception.CustomException;
import com.uhyeah.choolcheck.global.exception.StatusCode;
import com.uhyeah.choolcheck.web.user.CustomUserDetails;
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

        checkDuplication(employeeSaveRequestDto.getName(), employeeSaveRequestDto.getColor(), customUserDetails.getUser());

        employeeRepository.save(employeeSaveRequestDto.toEntity(customUserDetails.getUser()));
    }

    @Transactional
    public void delete(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> CustomException.builder()
                        .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                        .message("존재하지 않는 직원입니다.")
                        .fieldName("id")
                        .rejectValue(id.toString())
                        .build());
        employeeRepository.delete(employee);
    }

    @Transactional
    public void update(Long id, EmployeeUpdateRequestDto employeeUpdateReqestDto, CustomUserDetails customUserDetails) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> CustomException.builder()
                        .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                        .message("존재하지 않는 직원입니다.")
                        .fieldName("id")
                        .rejectValue(id.toString())
                        .build());

        String name = employeeUpdateReqestDto.getName();
        Color color = employeeUpdateReqestDto.getColor();

        if(employeeRepository.existsByNameAndColorAndUserAndIdNot(name, color, customUserDetails.getUser(), id)) {
            throw CustomException.builder()
                    .statusCode(StatusCode.DUPLICATE_RESOURCE)
                    .message("중복된 직원-색상 입니다.")
                    .fieldName("name-color")
                    .rejectValue(name + "-" + color)
                    .build();
        }
        employee.update(employeeUpdateReqestDto.getName(), employeeUpdateReqestDto.getRole(), employeeUpdateReqestDto.getColor());
    }


    @Transactional(readOnly = true)
    public EmployeeResponseDto getEmployee(Long id) {

        return employeeRepository.findById(id)
                .map(EmployeeResponseDto::new)
                .orElseThrow(() -> CustomException.builder()
                        .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                        .message("존재하지 않는 직원입니다.")
                        .fieldName("id")
                        .rejectValue(id.toString())
                        .build());
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> getEmployeeList(CustomUserDetails customUserDetails) {

        return employeeRepository.findByUser(customUserDetails.getUser()).stream()
                .map(EmployeeResponseDto::new)
                .collect(Collectors.toList());
    }


    private void checkDuplication(String name, Color color, User user) {
        if(employeeRepository.existsByNameAndColorAndUser(name, color, user)) {
            throw CustomException.builder()
                    .statusCode(StatusCode.DUPLICATE_RESOURCE)
                    .message("중복된 직원-색상 입니다.")
                    .fieldName("name-color")
                    .rejectValue(name + "-" + color)
                    .build();
        }
    }


}
