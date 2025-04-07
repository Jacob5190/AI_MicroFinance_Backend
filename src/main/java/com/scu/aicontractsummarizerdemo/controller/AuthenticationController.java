package com.scu.aicontractsummarizerdemo.controller;

import com.scu.aicontractsummarizerdemo.dto.LoginRequest;
import com.scu.aicontractsummarizerdemo.dto.RegisterRequest;
import com.scu.aicontractsummarizerdemo.dto.UserResponse;
import com.scu.aicontractsummarizerdemo.entity.Role;
import com.scu.aicontractsummarizerdemo.entity.User;
import com.scu.aicontractsummarizerdemo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request, HttpSession session) {
        if (userService.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email already in use");
        }

        User user = userService.registerUser(
                request.getEmail(),
                request.getPassword(),
                request.getFirstName(),
                request.getLastName(),
                request.getPhoneNumber(),
                request.getRole()
        );

        if (request.getRole() == Role.BORROWER && request.getBorrowerDetails() != null) {
            userService.createBorrowerDetails(user, request.getBorrowerDetails());
        } else if (request.getRole() == Role.LENDER && request.getLenderDetails() != null) {
            userService.createLenderDetails(user, request.getLenderDetails());
        }

        session.setAttribute("USER_ID", user.getId()); // Store user ID in session
        return ResponseEntity.ok(new UserResponse(user)); // Return user details
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpSession session) {
        Optional<User> user = userService.authenticateUser(request.getEmail(), request.getPassword());

        if (user.isPresent()) {
            session.setAttribute("USER_ID", user.get().getId());
            return ResponseEntity.ok(new UserResponse(user.get()));
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // Retrieve session if exists
        if (session != null) {
            session.invalidate(); // Destroy the session
        }
        return ResponseEntity.ok("User logged out successfully.");
    }
}
