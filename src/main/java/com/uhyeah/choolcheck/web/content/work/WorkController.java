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
    public ResponseEntity<Object> save(@Valid @RequestBody WorkSaveRequestDto requestDto) {

        workService.save(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @Valid @RequestBody WorkUpdateRequestDto requestDto) {

        workService.update(id, requestDto);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {

        workService.delete(id);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<WorkResponseDto> getWork(@PathVariable Long id) {

        return ResponseEntity.ok(workService.getWork(id));
    }


    @GetMapping("/month")
    public ResponseEntity<List<WorkResponseDto>> getWorkCalendar(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate date, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return ResponseEntity.ok(workService.getWorkCalendar(date, customUserDetails.getUser()));
    }


    @GetMapping()
    public ResponseEntity<Page<WorkResponseDto>> getWorkList(@RequestParam(name = "employee", required = false) Long employeeId, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate dateFrom,
                                                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateTo, @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                             @PageableDefault(size=10) Pageable pageable) {

        return ResponseEntity.ok(workService.getWorkList(customUserDetails.getUser(), employeeId, dateFrom, dateTo, pageable));
    }
}
