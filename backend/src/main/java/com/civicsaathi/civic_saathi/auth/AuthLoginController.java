package com.civicsaathi.civic_saathi.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthLoginController {

    private final UserAccountRepository users;
    private final org.springframework.security.crypto.password.PasswordEncoder encoder;
    private final org.springframework.security.authentication.AuthenticationManager authenticationManager;

    public AuthLoginController(
            UserAccountRepository users,
            org.springframework.security.crypto.password.PasswordEncoder encoder,
            org.springframework.security.authentication.AuthenticationManager authenticationManager) {
        this.users = users;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
    }

    public record LoginRequest(String email, String password) {}

    @GetMapping("/csrf")
    public Map<String, String> csrf(CsrfToken token) {
        return Map.of("token", token.getToken());
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) {

        if (request.email() == null || request.password() == null) {
            return ResponseEntity.badRequest()
                .body(Map.of("message", "Email and password are required."));
        }

        UserAccount user = users.findByEmailIgnoreCase(request.email().trim())
            .orElse(null);

        if (user == null || !user.isVerified()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Invalid credentials or email not verified."));
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    user.getEmail(), request.password()));

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            servletRequest.getSession(true);
            new HttpSessionSecurityContextRepository()
                .saveContext(context, servletRequest, servletResponse);

            return ResponseEntity.ok(Map.of(
                "message", "Login successful.",
                "email", user.getEmail()));
        } catch (AuthenticationException ex) {
            SecurityContextHolder.clearContext();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Invalid credentials or email not verified."));
        }
    }
}