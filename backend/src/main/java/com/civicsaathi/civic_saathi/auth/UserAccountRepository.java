package com.civicsaathi.civic_saathi.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserAccountRepository
        extends JpaRepository<UserAccount, Long> {

    Optional<UserAccount> findByEmailIgnoreCase(String email);

    Optional<UserAccount> findByVerificationToken(String token);

    boolean existsByEmailIgnoreCase(String email);
}
