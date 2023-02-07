package com.uhyeah.choolcheck.web.memo;

import com.uhyeah.choolcheck.web.memo.dto.MemoResponseDto;
import com.uhyeah.choolcheck.web.memo.dto.MemoSaveRequestDto;
import com.uhyeah.choolcheck.web.memo.dto.MemoUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/memo")
public class MemoController {

    public final MemoService memoService;

    @PostMapping
    public ResponseEntity save(@Valid @RequestBody MemoSaveRequestDto memoSaveRequestDto) {

        memoService.save(memoSaveRequestDto);

        return new ResponseEntity("메모작성 성공.", HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, @Valid @RequestBody MemoUpdateRequestDto memoUpdateRequestDto) {

        memoService.update(id, memoUpdateRequestDto);

        return new ResponseEntity("메모수정 성공.", HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {

        memoService.delete(id);

        return new ResponseEntity("메모삭제 성공.", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemoResponseDto> getMemo(@PathVariable Long id) {

        return new ResponseEntity(memoService.getMemo(id), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<MemoResponseDto> getMemoByDate(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        return new ResponseEntity(memoService.getMemoByDate(date), HttpStatus.OK);
    }

}
