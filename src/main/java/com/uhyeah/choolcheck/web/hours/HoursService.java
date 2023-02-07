package com.uhyeah.choolcheck.web.hours;

import com.uhyeah.choolcheck.domain.entity.Hours;
import com.uhyeah.choolcheck.domain.repository.HoursRepository;
import com.uhyeah.choolcheck.web.exception.CustomException;
import com.uhyeah.choolcheck.web.exception.StatusCode;
import com.uhyeah.choolcheck.web.hours.dto.HoursResponseDto;
import com.uhyeah.choolcheck.web.hours.dto.HoursSaveRequestDto;
import com.uhyeah.choolcheck.web.user.CustomUserDetails;
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
            throw CustomException.builder()
                    .statusCode(StatusCode.DUPLICATE_RESOURCE)
                    .message("중복된 근무형태명 입니다.")
                    .fieldName("title")
                    .rejectValue(hoursSaveRequestDto.getTitle())
                    .build();
        }
        hoursRepository.save(hoursSaveRequestDto.toEntity(customUserDetails.getUser()));
    }

    @Transactional
    public void delete(Long id) {

        Hours hours = hoursRepository.findById(id)
                        .orElseThrow(() -> CustomException.builder()
                                .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                                .message("존재하지 않는 근무형태입니다.")
                                .fieldName("id")
                                .rejectValue(id.toString())
                                .build());
        hoursRepository.delete(hours);
    }

    @Transactional(readOnly = true)
    public HoursResponseDto getHours(Long id) {

        return hoursRepository.findById(id)
                .map(HoursResponseDto::new)
                .orElseThrow(() -> CustomException.builder()
                        .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                        .message("존재하지 않는 근무형태입니다.")
                        .fieldName("id")
                        .rejectValue(id.toString())
                        .build());
    }

    @Transactional(readOnly = true)
    public List<HoursResponseDto> getHoursList(CustomUserDetails customUserDetails) {

        return hoursRepository.findByUser(customUserDetails.getUser()).stream()
                .map(HoursResponseDto::new)
                .collect(Collectors.toList());
    }
}
