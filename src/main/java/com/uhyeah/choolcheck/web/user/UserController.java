package com.uhyeah.choolcheck.web.user;

import com.uhyeah.choolcheck.web.user.dto.UserSaveRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity signup(@Valid @RequestBody UserSaveRequestDto userSaveRequestDto) {

        Long userId = userService.signup(userSaveRequestDto);

        if (userId == null) {
            return new ResponseEntity("존재하는 회원 이메일입니다.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity("회원가입 성공", HttpStatus.OK);
    }
}
