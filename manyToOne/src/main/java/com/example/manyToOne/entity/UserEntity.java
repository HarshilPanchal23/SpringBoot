package com.example.manyToOne.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "user_table")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserEntity extends BaseEntityAudit {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    // Many users belong to one organization
    @ManyToOne(fetch = FetchType.EAGER) // Eager fetching, load organization with user
    @JoinColumn(name = "organization_id", nullable = false)
    private OrganizationEntity organization;


    public UserEntity(long l) {
        this.id = l;
    }
}
