package com.example.userservice.service;

import com.example.userservice.dto.UserResponse;
import com.example.userservice.dto.UserSignupRequest;
import com.example.userservice.dto.UserLoginRequest;
import com.example.userservice.dto.UserUpdateRequest;
import com.example.userservice.entity.User;
import com.example.userservice.jwt.JwtUtil;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void signup(UserSignupRequest request) {
        log.info("회원가입 요청 수신: email={}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("회원가입 실패 - 중복 이메일: {}", request.getEmail());
            throw new RuntimeException("이미 사용 중인 이메일입니다.");
        }

        if (userRepository.existsByNickname(request.getNickname())) {
            log.warn("회원가입 실패 - 중복 닉네임: {}", request.getNickname());
            throw new RuntimeException("이미 사용 중인 닉네임입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        log.debug("비밀번호 암호화 완료");

        User user = User.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .nickname(request.getNickname())
                .build();

        userRepository.save(user);
        log.info("회원가입 성공: email={}, nickname={}", user.getEmail(), user.getNickname());
    }

    public String login(UserLoginRequest request) {
        log.info("로그인 요청: email={}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("로그인 실패 - 비밀번호 불일치: email={}", request.getEmail());
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        log.info("로그인 성공: email={}", user.getEmail());
        return token;
    }

    public UserResponse getMyInfo(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return new UserResponse(user);
    }

    @Transactional
    public UserResponse updateMyInfo(String email, UserUpdateRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        user.setNickname(request.getNickname());
        return new UserResponse(user);
    }
}
