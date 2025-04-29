package com.example.userservice.controller;

import com.example.userservice.dto.UserLoginRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.dto.UserSignupRequest;
import com.example.userservice.dto.UserUpdateRequest;
import com.example.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid UserSignupRequest request) {
        userService.signup(request);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid UserLoginRequest request) {
        String token = userService.login(request);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyInfo(Authentication authentication) {
        String email = (String) authentication.getPrincipal();
        return ResponseEntity.ok(userService.getMyInfo(email));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMyInfo(
            Authentication authentication,
            @RequestBody @Valid UserUpdateRequest request
    ) {
        String email = (String) authentication.getPrincipal();
        return ResponseEntity.ok(userService.updateMyInfo(email, request));
    }
}