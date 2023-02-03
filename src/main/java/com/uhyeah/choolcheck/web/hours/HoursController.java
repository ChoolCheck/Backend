package com.uhyeah.choolcheck.web.hours;

import com.uhyeah.choolcheck.web.hours.dto.HoursResponseDto;
import com.uhyeah.choolcheck.web.hours.dto.HoursSaveRequestDto;
import com.uhyeah.choolcheck.web.user.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/hours")
public class HoursController {

    private final HoursService hoursService;

    @PostMapping()
    public ResponseEntity save(@Valid @RequestBody HoursSaveRequestDto hoursSaveRequestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        hoursService.save(hoursSaveRequestDto, customUserDetails);

        return new ResponseEntity("근무형태 저장 완료.", HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {

        hoursService.delete(id);

        return new ResponseEntity("근무형태 삭제 완료.", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HoursResponseDto> getHours(@PathVariable Long id) {

        return new ResponseEntity(hoursService.getHours(id), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<HoursResponseDto>> getHoursList(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return new ResponseEntity(hoursService.getHoursList(customUserDetails), HttpStatus.OK);
    }


}
