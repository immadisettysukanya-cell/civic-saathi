package com.civicsaathi.civic_saathi.auth;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UserAccountRepository users;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String mailUsername;

    @Value("${civicsaathi.frontend-url:http://localhost:5173}")
    private String frontendUrl;

    private final SecureRandom secureRandom = new SecureRandom();

    public AuthService(
            UserAccountRepository users,
            PasswordEncoder passwordEncoder,
            JavaMailSender mailSender) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    @Transactional
    public String register(String email, String password) {
        String normalizedEmail = normalizeEmail(email);

        if (password == null || password.length() < 8
                || password.length() > 72) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Password must be between 8 and 72 characters.");
        }

        if (users.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "An account with this email already exists.");
        }

        if (mailUsername == null || mailUsername.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Email verification is not configured yet. "
                    + "Configure the email sender and try registration again.");
        }
        UserAccount user = new UserAccount(
                normalizedEmail,
                passwordEncoder.encode(password));

        byte[] tokenBytes = new byte[32];
        secureRandom.nextBytes(tokenBytes);

        String token = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(tokenBytes);

        user.setVerificationToken(token);
        user.setVerificationTokenExpiry(
                Instant.now().plusSeconds(30 * 60));

        users.saveAndFlush(user);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailUsername);
            message.setTo(normalizedEmail);
            message.setSubject("Verify your CivicSaathi email");
            message.setText(
                    "Welcome to CivicSaathi!\n\n"
                    + "Verify your email using this link:\n"
                    + frontendUrl
                    + "/?token=" + token
                    + "\n\nThis link expires in 30 minutes."
                    + "\nIf you did not create this account, ignore this email.");

            mailSender.send(message);
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "We could not send the verification email. "
                    + "Check the email configuration and try again.");
        }

        return "Registration received. Check your email to verify your account.";
    }

    @Transactional
    public String verifyEmail(String token) {
        if (token == null || token.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Verification token is required.");
        }

        UserAccount user = users.findByVerificationToken(token)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Invalid or expired verification link."));

        if (user.getVerificationTokenExpiry() == null
                || !user.getVerificationTokenExpiry().isAfter(Instant.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid or expired verification link.");
        }

        user.setVerified(true);
        user.setVerificationToken(null);
        user.setVerificationTokenExpiry(null);
        users.save(user);

        return "Email verified successfully. You can now log in.";
    }

    private String normalizeEmail(String email) {
        if (email == null || email.isBlank()
                || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Enter a valid email address.");
        }
        return email.trim().toLowerCase(Locale.ROOT);
    }
}