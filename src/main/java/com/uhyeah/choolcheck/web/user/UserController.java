package com.uhyeah.choolcheck.web.user;

import com.uhyeah.choolcheck.domain.entity.User;
import com.uhyeah.choolcheck.web.exception.StatusCode;
import com.uhyeah.choolcheck.web.exception.StatusResponseDto;
import com.uhyeah.choolcheck.web.user.dto.UserLoginRequestDto;
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

        userService.signup(userSaveRequestDto);

        return new ResponseEntity(StatusCode.SIGNUP_SUCCESS.getMessage(), StatusCode.SIGNUP_SUCCESS.getHttpStatus());
    }

//    @PostMapping("/login")
//    public ResponseEntity login(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto) {
//
//        User user = userService.login(userLoginRequestDto);
//
//        if (user == null) {
//            return new ResponseEntity("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
//        }
//
//        return new ResponseEntity();
//    }
}