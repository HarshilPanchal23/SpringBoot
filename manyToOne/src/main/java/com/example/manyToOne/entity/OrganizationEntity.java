package com.example.manyToOne.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Table(name = "organization_table")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
public class OrganizationEntity extends BaseEntityAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "organization_name", nullable = false, unique = true)
    private String organizationName;

    @Column(name = "address")
    private String address;

    public OrganizationEntity(Long id) {
        this.id = id;
    }

}
