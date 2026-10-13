package com.civicsaathi.civic_saathi.auth;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
    name = "civic_users",
    uniqueConstraints = @UniqueConstraint(columnNames = "email")
)
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 254)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;

    @Column(nullable = false)
    private boolean verified = false;

    @Column(name = "verification_token", length = 64)
    private String verificationToken;

    @Column(name = "verification_token_expiry")
    private Instant verificationTokenExpiry;

    protected UserAccount() {
    }

    public UserAccount(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public boolean isVerified() {
        return verified;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public Instant getVerificationTokenExpiry() {
        return verificationTokenExpiry;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public void setVerificationTokenExpiry(Instant expiry) {
        this.verificationTokenExpiry = expiry;
    }
}
