package org.mabs.repository;

import org.mabs.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    //check token when user click link send on email(token.isExisted())
    Optional<PasswordResetToken> findByToken(String token);
}
