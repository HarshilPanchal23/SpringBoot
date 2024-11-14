package com.example.manyToOne.repository;


import com.example.manyToOne.dto.UserResponseDto;
import com.example.manyToOne.entity.OrganizationEntity;
import com.example.manyToOne.entity.UserEntity;
import com.example.manyToOne.projection.UserProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface UserRepository extends JpaRepository<com.example.manyToOne.entity.UserEntity, Long> {


    Optional<UserEntity> findByEmail(String email);

    Page<UserEntity> findByFirstNameLike(String firstName, Pageable pageable);

    //    @Query("select u.firstName, u.lastName,u.email,u.password, u.status, u.organization.id from UserEntity u where u.organization.id = :id")
//    @Query(nativeQuery = true, value = "select u.first_name as firstName, u.last_name as lastName,u.email as email,u.password as password, u.status as status, u.deactivate as deactivate from user_table u where organization_id = ?1")
    @Query(nativeQuery = true, value = "SELECT u.first_name AS firstName, u.last_name AS lastName, u.email AS email, " +
            "u.password AS password, u.status AS status, u.deactivate AS deactivate, " +
            "ot.organization_name AS organizationName, ot.address AS address " +
            "FROM user_table u " +
            "INNER JOIN organization_table ot ON ot.id = u.organization_id " +
            "WHERE ot.id = :id")
    Page<UserProjection> findByOrganizationId(@Param("id") Long organizationId, Pageable pageable);
}


