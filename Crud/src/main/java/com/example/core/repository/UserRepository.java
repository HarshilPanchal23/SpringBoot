package com.example.core.repository;

import com.example.core.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<UserEntity,Long> {


    Optional<UserEntity> findByEmail(String email);

    Page<UserEntity> findByFirstNameLike(String firstName, Pageable pageable);
}


