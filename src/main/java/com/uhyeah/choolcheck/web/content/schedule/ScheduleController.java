package com.uhyeah.choolcheck.web.content.schedule;

import com.uhyeah.choolcheck.web.content.schedule.dto.ScheduleResponseDto;
import com.uhyeah.choolcheck.web.content.schedule.dto.ScheduleSaveRequestDto;
import com.uhyeah.choolcheck.web.content.schedule.dto.ScheduleUpdateRequestDto;
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
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;


    @PostMapping
    public ResponseEntity<Object> save(@Valid @RequestBody ScheduleSaveRequestDto scheduleSaveRequestDto) {

        scheduleService.save(scheduleSaveRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @Valid @RequestBody ScheduleUpdateRequestDto scheduleUpdateRequestDto) {

        scheduleService.update(id, scheduleUpdateRequestDto);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {

        scheduleService.delete(id);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> getSchedule(@PathVariable Long id) {

        return ResponseEntity.ok(scheduleService.getSchedule(id));
    }


    @GetMapping("/month")
    public ResponseEntity<List<ScheduleResponseDto>> getScheduleCalendar(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate date, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return ResponseEntity.ok(scheduleService.getScheduleCalendar(date, customUserDetails));
    }


    @GetMapping("/week")
    public ResponseEntity<List<List<ScheduleResponseDto>>> getScheduleByWeek(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return ResponseEntity.ok(scheduleService.getScheduleByWeek(customUserDetails));
    }

    @GetMapping()
    public ResponseEntity<Page<ScheduleResponseDto>> getScheduleList(@RequestParam(name = "employee", required = false) Long employeeId, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate dateFrom,
                                                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateTo, @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                     @PageableDefault(size=10) Pageable pageable) {

        return ResponseEntity.ok(scheduleService.getScheduleList(customUserDetails, employeeId, dateFrom, dateTo, pageable));
    }
}
