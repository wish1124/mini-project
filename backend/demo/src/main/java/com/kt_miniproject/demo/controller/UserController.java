package com.kt_miniproject.demo.controller;

import com.kt_miniproject.demo.domain.user.User;
import com.kt_miniproject.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")   // ★ /api/users 로 통일
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    // 1. 유저 생성
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User saved = userRepository.save(user);
        return ResponseEntity.ok(saved);
    }

    // 2. 유저 조회
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return ResponseEntity.ok(user);
    }
}
