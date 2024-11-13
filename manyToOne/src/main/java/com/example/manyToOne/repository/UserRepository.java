package com.example.manyToOne.repository;


import com.example.manyToOne.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<com.example.manyToOne.entity.UserEntity,Long> {


    Optional<UserEntity> findByEmail(String email);

    Page<UserEntity> findByFirstNameLike(String firstName, Pageable pageable);
}


