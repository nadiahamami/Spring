package com.vermeg.repositories;

import com.vermeg.entities.PasswordResetToken;
import com.vermeg.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {
    boolean existsByUser(User user);
    PasswordResetToken getByUser(User user);
    PasswordResetToken getByToken(String token);
}
