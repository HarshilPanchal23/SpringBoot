package com.example.springSecurity.entity;

import com.example.springSecurity.dto.BaseEntityAudit;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.id.IntegralDataTypeHolder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;

@Table(name = "grade_table")
@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@SuperBuilder
@ToString
public class GradeEntity extends BaseEntityAudit implements Serializable {

    @Serial
    private static final long serialVersionUID = 890006816268488368L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_grade")
    private Integer totalGrade;

    // Many users belong to one organization
    @OneToOne(fetch = FetchType.EAGER) // Eager fetching, load organization with user
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;
}
