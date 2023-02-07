package com.uhyeah.choolcheck.web.user;

import com.uhyeah.choolcheck.web.user.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

        return new ResponseEntity("회원가입 성공.", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto) {

        return new ResponseEntity(userService.login(userLoginRequestDto), HttpStatus.OK);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponseDto> reissue (@RequestHeader(value = "refreshToken") String refreshToken) {

        return new ResponseEntity(userService.reissue(refreshToken), HttpStatus.CREATED);
    }


    @DeleteMapping()
    public ResponseEntity delete(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        userService.delete(customUserDetails);

        return new ResponseEntity("회원탈퇴 성공.", HttpStatus.OK);
    }


    @PatchMapping()
    public ResponseEntity update(@Valid @RequestBody UserUpdateRequestDto userUpdateRequestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        userService.update(userUpdateRequestDto, customUserDetails);

        return new ResponseEntity("회원수정 성공.", HttpStatus.OK);
    }


    @PatchMapping("/password")
    public ResponseEntity updatePassword(@Valid @RequestBody UserPasswordUpdateRequestDto userPasswordUpdateRequestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        userService.updatePassword(userPasswordUpdateRequestDto, customUserDetails);

        return new ResponseEntity("비밀번호 변경 성공.", HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<UserResponseDto> getUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return new ResponseEntity(userService.getUser(customUserDetails), HttpStatus.OK);
    }
}
