package com.uhyeah.choolcheck.web.employee;

import com.uhyeah.choolcheck.web.employee.dto.EmployeeResponseDto;
import com.uhyeah.choolcheck.web.employee.dto.EmployeeSaveRequestDto;
import com.uhyeah.choolcheck.web.employee.dto.EmployeeUpdateRequestDto;
import com.uhyeah.choolcheck.web.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity save(@Valid @RequestBody EmployeeSaveRequestDto employeeSaveRequestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        employeeService.save(employeeSaveRequestDto, customUserDetails);

        return new ResponseEntity("직원저장 성공.", HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {

        employeeService.delete(id);

        return new ResponseEntity("직원삭제 성공.", HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, @Valid @RequestBody EmployeeUpdateRequestDto employeeUpdateRequestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        employeeService.update(id, employeeUpdateRequestDto, customUserDetails);

        return new ResponseEntity("직원수정 성공.", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> getEmployee(@PathVariable Long id) {

        return new ResponseEntity(employeeService.getEmployee(id), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<EmployeeResponseDto>> getEmployeeList(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return new ResponseEntity<>(employeeService.getEmployeeList(customUserDetails), HttpStatus.OK);
    }


}
