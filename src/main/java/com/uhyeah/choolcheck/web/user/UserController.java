package com.uhyeah.choolcheck.web.user;

import com.uhyeah.choolcheck.web.user.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@Valid @RequestBody UserSaveRequestDto requestDto) {

        userService.signup(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PostMapping("/email")
    public ResponseEntity<Object> verifyEmail(@Valid @RequestBody EmailValidateRequestDto requestDto) {

        userService.sendVerifyEmailMail(requestDto);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody UserLoginRequestDto requestDto, @RequestHeader("X-FORWARDED-FOR") String ip) {

        return ResponseEntity.ok(userService.login(requestDto, ip));
    }


    @PostMapping("/reissue")
    public ResponseEntity<TokenResponseDto> reissue (@RequestHeader(value = "accessToken") String accessToken, @RequestHeader(value = "refreshToken") String refreshToken,
                                                     @RequestHeader("X-FORWARDED-FOR") String ip) {

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.reissue(accessToken, refreshToken, ip));
    }


    @PostMapping("/logout")
    public ResponseEntity<Object> logout(@RequestHeader(value = "Authorization") String bearerToken, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        userService.logout(bearerToken);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping()
    public ResponseEntity<Object> delete(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        userService.delete(customUserDetails.getUser());
        return ResponseEntity.ok().build();
    }


    @PatchMapping()
    public ResponseEntity<Object> update(@Valid @RequestBody UserUpdateRequestDto requestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        userService.update(requestDto, customUserDetails.getUser());
        return ResponseEntity.ok().build();
    }


    @PostMapping("/password")
    public ResponseEntity<Object> sendUpdatePasswordEmail(@RequestHeader(value = "Authorization") String bearerToken, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        userService.sendUpdatePasswordMail(bearerToken, customUserDetails.getUser());
        return ResponseEntity.ok().build();
    }


    @PatchMapping("/password")
    public ResponseEntity<Object> updatePassword(@Valid @RequestBody PasswordUpdateRequestDto requestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        userService.updatePassword(requestDto, customUserDetails.getUser());
        return ResponseEntity.ok().build();
    }


    @GetMapping()
    public ResponseEntity<UserResponseDto> getUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return ResponseEntity.ok(userService.getUser(customUserDetails.getUser()));
    }
}
