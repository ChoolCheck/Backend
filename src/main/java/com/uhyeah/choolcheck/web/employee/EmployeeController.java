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
    public ResponseEntity<Object> save(@Valid @RequestBody EmployeeSaveRequestDto employeeSaveRequestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        employeeService.save(employeeSaveRequestDto, customUserDetails);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @Valid @RequestBody EmployeeUpdateRequestDto employeeUpdateRequestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        employeeService.update(id, employeeUpdateRequestDto, customUserDetails);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {

        employeeService.delete(id);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> getEmployee(@PathVariable Long id) {

        return ResponseEntity.ok(employeeService.getEmployee(id));
    }

    @GetMapping()
    public ResponseEntity<List<EmployeeResponseDto>> getEmployeeList(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return ResponseEntity.ok(employeeService.getEmployeeList(customUserDetails));
    }
}
