package com.demo.spring_security_jwt.entity;


import com.demo.spring_security_jwt.dto.BaseEntityAudit;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;

@Table(name = "user_table")
@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@SuperBuilder
@ToString
public class UserEntity extends BaseEntityAudit implements Serializable {

    @Serial
    private static final long serialVersionUID = 890006816268488368L;

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

    @Column(name = "login_counter")
    private Long loginCounter = 0L;

    @Column(name = "account_non_locked")
    private Boolean accountNonLocked = false;

    public UserEntity(long l) {
        this.id = l;
    }
}
