package com.expensetracker.auth.controller;

import com.expensetracker.auth.dto.AuthResponse;
import com.expensetracker.auth.dto.LoginRequest;
import com.expensetracker.auth.dto.RegisterRequest;
import com.expensetracker.auth.util.JwtUtil;
import com.expensetracker.user.entity.User;
import com.expensetracker.user.entity.UserRole;
import com.expensetracker.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {

        // 1. Check if email already exists
        if (userService.existsByEmail(request.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Email is already registered");
        }

        // 2. Create User entity
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.USER)
                .enabled(true)
                .build();

        // 3. Save user
        userService.saveUser(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {

        System.out.println("🔥 LOGIN CONTROLLER HIT 🔥");

        return userService.findByEmail(request.getEmail())
                .map(user -> {
                    System.out.println("➡ DB PASSWORD: " + user.getPassword());
                    System.out.println("➡ RAW PASSWORD: " + request.getPassword());

                    boolean match = passwordEncoder.matches(
                            request.getPassword(),
                            user.getPassword()
                    );

                    System.out.println("➡ PASSWORD MATCH RESULT: " + match);

                    if (!match) {
                        return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body("Invalid email or password");
                    }

                    String token = jwtUtil.generateToken(
                            user.getId(),
                            user.getEmail(),
                            user.getRole().name()
                    );

                    return ResponseEntity.ok(new AuthResponse(token));
                })
                .orElse(
                        ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body("Invalid email or password")
                );
    }




}
