package com.uhyeah.choolcheck.web.memo;

import com.uhyeah.choolcheck.web.memo.dto.MemoResponseDto;
import com.uhyeah.choolcheck.web.memo.dto.MemoSaveRequestDto;
import com.uhyeah.choolcheck.web.memo.dto.MemoUpdateRequestDto;
import com.uhyeah.choolcheck.web.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/memo")
public class MemoController {

    public final MemoService memoService;

    @PostMapping
    public ResponseEntity<Object> save(@Valid @RequestBody MemoSaveRequestDto memoSaveRequestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        memoService.save(memoSaveRequestDto, customUserDetails);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @Valid @RequestBody MemoUpdateRequestDto memoUpdateRequestDto) {

        memoService.update(id, memoUpdateRequestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {

        memoService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemoResponseDto> getMemo(@PathVariable Long id) {

        return ResponseEntity.ok(memoService.getMemo(id));
    }

    @GetMapping()
    public ResponseEntity<List<MemoResponseDto>> getMemoByDate(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return ResponseEntity.ok(memoService.getMemoByDate(date, customUserDetails));
    }

}
