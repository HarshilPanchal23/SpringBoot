package com.demo.spring_security_jwt.repository;

import com.demo.spring_security_jwt.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity ,Long> {

    Optional<RoleEntity> findByRole(String role);
}
