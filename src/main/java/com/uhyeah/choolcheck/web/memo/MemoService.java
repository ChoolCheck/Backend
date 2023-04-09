package com.uhyeah.choolcheck.web.memo;

import com.uhyeah.choolcheck.domain.entity.Memo;
import com.uhyeah.choolcheck.domain.entity.User;
import com.uhyeah.choolcheck.domain.repository.MemoRepository;
import com.uhyeah.choolcheck.global.exception.CustomException;
import com.uhyeah.choolcheck.global.exception.StatusCode;
import com.uhyeah.choolcheck.web.memo.dto.MemoResponseDto;
import com.uhyeah.choolcheck.web.memo.dto.MemoSaveRequestDto;
import com.uhyeah.choolcheck.web.memo.dto.MemoUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MemoService {

    private final MemoRepository memoRepository;

    @Transactional
    public void save(MemoSaveRequestDto requestDto, User loginUser) {

        memoRepository.save(requestDto.toEntity(loginUser));
    }

    @Transactional
    public void update(Long id, MemoUpdateRequestDto requestDto) {

        Memo memo = memoRepository.findById(id)
                .orElseThrow(() -> CustomException.builder()
                        .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                        .message("존재하지 않는 메모입니다.")
                        .fieldName("id")
                        .rejectValue(id.toString())
                        .build());

        memo.update(requestDto.getDate(), requestDto.getContent());
    }

    @Transactional
    public void delete(Long id) {

        Memo memo = memoRepository.findById(id)
                .orElseThrow(() -> CustomException.builder()
                        .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                        .message("존재하지 않는 메모입니다.")
                        .fieldName("id")
                        .rejectValue(id.toString())
                        .build());

        memoRepository.delete(memo);
    }

    @Transactional(readOnly = true)
    public MemoResponseDto getMemo(Long id) {

        return memoRepository.findById(id)
                .map(MemoResponseDto::new)
                .orElseThrow(() -> CustomException.builder()
                        .statusCode(StatusCode.RESOURCE_NOT_FOUND)
                        .message("존재하지 않는 메모입니다.")
                        .fieldName("id")
                        .rejectValue(id.toString())
                        .build());
    }

    @Transactional(readOnly = true)
    public List<MemoResponseDto> getMemoByDate(LocalDate date, User loginUser) {

        return memoRepository.findByUserAndDate(loginUser, date).stream()
                .map(MemoResponseDto::new)
                .collect(Collectors.toList());
    }

}
