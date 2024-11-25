package com.example.springSecurity.repository;

import com.example.springSecurity.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity ,Long> {
}
