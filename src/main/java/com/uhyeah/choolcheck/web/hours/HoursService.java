package com.uhyeah.choolcheck.web.hours;

import com.uhyeah.choolcheck.domain.entity.Hours;
import com.uhyeah.choolcheck.domain.entity.User;
import com.uhyeah.choolcheck.domain.repository.HoursRepository;
import com.uhyeah.choolcheck.domain.repository.ScheduleRepository;
import com.uhyeah.choolcheck.domain.repository.WorkRepository;
import com.uhyeah.choolcheck.global.exception.CustomException;
import com.uhyeah.choolcheck.global.exception.StatusCode;
import com.uhyeah.choolcheck.web.hours.dto.HoursResponseDto;
import com.uhyeah.choolcheck.web.hours.dto.HoursSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class HoursService {

    private final HoursRepository hoursRepository;
    private final ScheduleRepository scheduleRepository;
    private final WorkRepository workRepository;

    @Transactional
    public void save(HoursSaveRequestDto requestDto, User loginUser) {

        checkTitleDuplication(requestDto.getTitle(), loginUser);
        hoursRepository.save(requestDto.toEntity(loginUser));
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

        scheduleRepository.setHoursNull(hours);
        workRepository.setHoursNull(hours);

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
    public List<HoursResponseDto> getHoursList(User loginUser) {

        return hoursRepository.findByUser(loginUser).stream()
                .map(HoursResponseDto::new)
                .collect(Collectors.toList());
    }


    private void checkTitleDuplication(String title, User loginUser) {

        if (hoursRepository.existsByTitleAndUser(title, loginUser)) {
            throw CustomException.builder()
                    .statusCode(StatusCode.DUPLICATE_RESOURCE)
                    .message("중복된 근무형태명 입니다.")
                    .fieldName("title")
                    .rejectValue(title)
                    .build();
        }
    }
}
