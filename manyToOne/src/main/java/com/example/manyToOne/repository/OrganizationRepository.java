package com.example.manyToOne.repository;

import com.example.manyToOne.entity.OrganizationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<OrganizationEntity, Long> {


    Page<OrganizationEntity> findByOrganizationNameLike(String organizationName, Pageable pageable);

    Optional<OrganizationEntity> findByOrganizationName(String organizationName);
}
