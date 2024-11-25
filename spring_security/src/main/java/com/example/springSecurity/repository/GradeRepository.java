package com.example.springSecurity.repository;

import com.example.springSecurity.dto.GradeProjection;
import com.example.springSecurity.entity.GradeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeRepository extends JpaRepository<GradeEntity, Long> {

    @Query(nativeQuery = true,value = "select gt.total_grade AS totalGrade,ut.first_name AS firstName from grade_table gt Inner join user_table ut on ut.id =gt.user_id where ut.id =:userId")
    Page<GradeProjection> findByUserId(@Param("userId") Long userId, Pageable pageable);

}
