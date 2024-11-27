package com.demo.spring_security_jwt.entity;


import com.demo.spring_security_jwt.dto.BaseEntityAudit;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Table(name = "role_table")
@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@SuperBuilder
@ToString
public class RoleEntity extends BaseEntityAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_name")
    private String role;
}
