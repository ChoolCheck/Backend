package com.uhyeah.choolcheck.web.statistics;

import com.uhyeah.choolcheck.web.user.CustomUserDetails;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping
    public ResponseEntity<List<StatisticsResponseDto>> getStatistics(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return new ResponseEntity(statisticsService.getStatistics(start, end, customUserDetails), HttpStatus.OK);
    }
}
