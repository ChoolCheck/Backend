package com.uhyeah.choolcheck.web.schedule;

import com.uhyeah.choolcheck.web.schedule.dto.ScheduleResponseDto;
import com.uhyeah.choolcheck.web.schedule.dto.ScheduleSaveRequestDto;
import com.uhyeah.choolcheck.web.schedule.dto.ScheduleUpdateRequestDto;
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
    public ResponseEntity save(@Valid @RequestBody ScheduleSaveRequestDto scheduleSaveRequestDto) {

        scheduleService.save(scheduleSaveRequestDto);
        return new ResponseEntity("스케줄저장 성공.", HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, @Valid @RequestBody ScheduleUpdateRequestDto scheduleUpdateRequestDto) {

        scheduleService.update(id, scheduleUpdateRequestDto);
        return new ResponseEntity("스케줄수정 성공.", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {

        scheduleService.delete(id);
        return new ResponseEntity("스케줄삭제 성공.", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> getSchedule(@PathVariable Long id) {

        return new ResponseEntity(scheduleService.getSchedule(id), HttpStatus.OK);
    }
    

    @GetMapping("/week")
    public ResponseEntity<List<List<ScheduleResponseDto>>> getScheduleByWeek(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return new ResponseEntity(scheduleService.getScheduleByWeek(customUserDetails), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Page<ScheduleResponseDto>> getScheduleList(@RequestParam(name = "employee", required = false) Long employeeId, @RequestParam(required = false) String period, @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                             @PageableDefault(size=10) Pageable pageable) {

        return new ResponseEntity(scheduleService.getScheduleList(customUserDetails, employeeId, period, pageable), HttpStatus.OK);
    }

    @GetMapping("/month")
    public ResponseEntity<List<ScheduleResponseDto>> getScheduleCalendar(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate date, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return new ResponseEntity(scheduleService.getScheduleCalendar(date, customUserDetails), HttpStatus.OK);
    }

}
