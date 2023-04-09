package com.uhyeah.choolcheck.web.user;

import com.uhyeah.choolcheck.web.user.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@Valid @RequestBody UserSaveRequestDto userSaveRequestDto) {

        userService.signup(userSaveRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PostMapping("/email")
    public ResponseEntity<Object> verifyEmail(@Valid @RequestBody EmailValidateRequestDto emailValidateRequestDto) {

        userService.sendVerifyEmailMail(emailValidateRequestDto);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto, @RequestHeader("X-FORWARDED-FOR") String ip) {

        System.out.println(ip);
        return ResponseEntity.ok(userService.login(userLoginRequestDto, ip));
    }


    @PostMapping("/reissue")
    public ResponseEntity<TokenResponseDto> reissue (@RequestHeader(value = "accessToken") String accessToken, @RequestHeader(value = "refreshToken") String refreshToken, HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.reissue(accessToken, refreshToken, request.getRemoteAddr()));
    }


    @PostMapping("/logout")
    public ResponseEntity<Object> logout(@RequestHeader(value = "Authorization") String bearerToken, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        userService.logout(bearerToken, customUserDetails);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping()
    public ResponseEntity<Object> delete(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        userService.delete(customUserDetails);
        return ResponseEntity.ok().build();
    }


    @PatchMapping()
    public ResponseEntity<Object> update(@Valid @RequestBody UserUpdateRequestDto userUpdateRequestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        userService.update(userUpdateRequestDto, customUserDetails);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/password")
    public ResponseEntity<Object> sendUpdatePasswordEmail(@RequestHeader(value = "Authorization") String bearerToken, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        userService.sendUpdatePasswordMail(bearerToken, customUserDetails);
        return ResponseEntity.ok().build();
    }


    @PatchMapping("/password")
    public ResponseEntity<Object> updatePassword(@Valid @RequestBody PasswordUpdateRequestDto passwordUpdateRequestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        userService.updatePassword(passwordUpdateRequestDto, customUserDetails);
        return ResponseEntity.ok().build();
    }


    @GetMapping()
    public ResponseEntity<UserResponseDto> getUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return ResponseEntity.ok(userService.getUser(customUserDetails));
    }
}
