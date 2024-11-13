package com.example.core.repository;

import com.example.core.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {


    Optional<UserEntity> findByIdAndStatusIsTrue(Long id);

    Optional<UserEntity> findByEmail(String email);

    Page<UserEntity> findByFirstNameLike(String firstName, Pageable pageable);
}


