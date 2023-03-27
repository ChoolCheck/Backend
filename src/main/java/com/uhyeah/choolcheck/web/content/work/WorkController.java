package com.uhyeah.choolcheck.web.content.work;

import com.uhyeah.choolcheck.web.content.work.dto.WorkResponseDto;
import com.uhyeah.choolcheck.web.content.work.dto.WorkUpdateRequestDto;
import com.uhyeah.choolcheck.web.content.work.dto.WorkSaveRequestDto;
import com.uhyeah.choolcheck.web.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/work")
public class WorkController {

    private final WorkService workService;

    @PostMapping
    public ResponseEntity save(@Valid @RequestBody WorkSaveRequestDto workSaveRequestDto) {

        workService.save(workSaveRequestDto);
        return new ResponseEntity("출근부저장 성공.", HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, @Valid @RequestBody WorkUpdateRequestDto workUpdateRequestDto) {

        workService.update(id, workUpdateRequestDto);
        return new ResponseEntity("출근부수정 성공.", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {

        workService.delete(id);
        return new ResponseEntity("출근부삭제 성공.", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkResponseDto> getWork(@PathVariable Long id) {

        return new ResponseEntity(workService.getWork(id), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Page<WorkResponseDto>> getWorkList(@RequestParam(name = "employee", required = false) Long employeeId, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate dateFrom,
                                                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateTo, @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                             @PageableDefault(size=10) Pageable pageable) {

        return new ResponseEntity(workService.getWorkList(customUserDetails, employeeId, dateFrom, dateTo, pageable), HttpStatus.OK);
    }

    @GetMapping("/month")
    public ResponseEntity<List<WorkResponseDto>> getWorkCalendar(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate date, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return new ResponseEntity(workService.getWorkCalendar(date, customUserDetails), HttpStatus.OK);
    }
}
