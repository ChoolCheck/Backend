package com.uhyeah.choolcheck.web.hours;

import com.uhyeah.choolcheck.web.hours.dto.HoursResponseDto;
import com.uhyeah.choolcheck.web.hours.dto.HoursSaveRequestDto;
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
@RequestMapping("/hours")
public class HoursController {

    private final HoursService hoursService;

    @PostMapping()
    public ResponseEntity<Object> save(@Valid @RequestBody HoursSaveRequestDto requestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        hoursService.save(requestDto, customUserDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {

        hoursService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HoursResponseDto> getHours(@PathVariable Long id) {

        return ResponseEntity.ok(hoursService.getHours(id));
    }

    @GetMapping()
    public ResponseEntity<List<HoursResponseDto>> getHoursList(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return ResponseEntity.ok(hoursService.getHoursList(customUserDetails.getUser()));
    }


}
