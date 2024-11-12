package com.example.core.entity;

import com.example.core.dto.BaseEntityAudit;
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

    public UserEntity(long l) {
        this.id=l;
    }
}
