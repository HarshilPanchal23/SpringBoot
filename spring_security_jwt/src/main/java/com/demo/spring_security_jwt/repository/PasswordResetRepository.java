package com.demo.spring_security_jwt.repository;

import com.demo.spring_security_jwt.entity.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordReset,Long> {
    PasswordReset findByToken(String token);

}
