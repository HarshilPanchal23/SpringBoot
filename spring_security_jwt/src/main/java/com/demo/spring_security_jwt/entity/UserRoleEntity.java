package com.demo.spring_security_jwt.entity;

import com.demo.spring_security_jwt.dto.BaseEntityAudit;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
@Table(name = "user_role_table")
@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@SuperBuilder
@ToString
public class UserRoleEntity extends BaseEntityAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity roleId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userId;



}
