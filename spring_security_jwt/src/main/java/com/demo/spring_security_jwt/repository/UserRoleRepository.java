package com.demo.spring_security_jwt.repository;

import com.demo.spring_security_jwt.entity.RoleEntity;
import com.demo.spring_security_jwt.entity.UserEntity;
import com.demo.spring_security_jwt.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity,Long> {

    Optional<UserRoleEntity> findByUserIdAndRoleId(UserEntity userId, RoleEntity roleId);

    @Query(nativeQuery = true,value = "select rt.role_name from user_role_table urt inner join role_table rt on rt.id = urt.role_id where urt.user_id = :id")
    List<String> findByUserId(@Param("id") Long id);

    List<UserRoleEntity> findByUserId(UserEntity userId);

}
