package com.uhyeah.choolcheck.web.hours;

import com.uhyeah.choolcheck.domain.entity.Hours;
import com.uhyeah.choolcheck.domain.repository.HoursRepository;
import com.uhyeah.choolcheck.web.exception.CustomException;
import com.uhyeah.choolcheck.web.exception.StatusCode;
import com.uhyeah.choolcheck.web.hours.dto.HoursResponseDto;
import com.uhyeah.choolcheck.web.hours.dto.HoursSaveRequestDto;
import com.uhyeah.choolcheck.web.user.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class HoursService {

    private final HoursRepository hoursRepository;

    @Transactional
    public void save(HoursSaveRequestDto hoursSaveRequestDto, CustomUserDetails customUserDetails) {

        if (hoursRepository.existsByTitle(hoursSaveRequestDto.getTitle())) {
            throw new CustomException(StatusCode.DUPLICATE_RESOURCE, "[Hours] title: "+ hoursSaveRequestDto.getTitle());
        }
        hoursRepository.save(hoursSaveRequestDto.toEntity(customUserDetails.getUser()));
    }

    @Transactional
    public void delete(Long id) {

        Hours hours = hoursRepository.findById(id)
                        .orElseThrow(() -> new CustomException(StatusCode.RESOURCE_NOT_FOUND, "[Hours] id:" + id));
        hoursRepository.delete(hours);
    }

    @Transactional(readOnly = true)
    public HoursResponseDto getHours(Long id) {

        return hoursRepository.findById(id)
                .map(HoursResponseDto::new)
                .orElseThrow(() -> new CustomException(StatusCode.RESOURCE_NOT_FOUND, "[Hours] id:" + id));
    }

    @Transactional(readOnly = true)
    public List<HoursResponseDto> getHoursList(CustomUserDetails customUserDetails) {

        return hoursRepository.findByUser(customUserDetails.getUser()).stream()
                .map(HoursResponseDto::new)
                .collect(Collectors.toList());
    }
}
