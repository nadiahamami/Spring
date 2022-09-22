package com.vermeg.repositories;

import com.vermeg.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // return True if email already exist
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, long id);
    // find user by email address
    User findByEmail(String email);
}
