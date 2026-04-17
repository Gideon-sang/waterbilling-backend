package com.estate.waterbilling.controller;


import com.estate.waterbilling.dto.LoginRequest;
import com.estate.waterbilling.dto.LoginResponse;
import com.estate.waterbilling.model.Manager;
import com.estate.waterbilling.repository.ManagerRepository;
import com.estate.waterbilling.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final ManagerRepository managerRepository;
    private final PasswordEncoder   passwordEncoder;
    private final JwtUtil           jwtUtil;

    public AuthController(ManagerRepository managerRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil) {
        this.managerRepository = managerRepository;
        this.passwordEncoder   = passwordEncoder;
        this.jwtUtil           = jwtUtil;
    }

    /**
     * POST /auth/login
     * Body: { "username": "admin", "password": "yourpassword" }
     * Returns: { "token": "eyJ...", "username": "admin" }
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        // 1. Find manager by username
        Manager manager = managerRepository
                .findByUsername(request.getUsername())
                .orElse(null);

        // 2. Check password
        if (manager == null || !passwordEncoder.matches(request.getPassword(), manager.getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password.");
        }

        // 3. Generate JWT
        String token = jwtUtil.generateToken(manager.getUsername());

        // 4. Return token + username
        return ResponseEntity.ok(new LoginResponse(token, manager.getUsername()));
    }
}

