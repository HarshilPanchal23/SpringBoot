package com.example.springSecurity.dto;

import com.example.springSecurity.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@SuperBuilder
@ToString
public abstract class BaseEntityAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedDate;

    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    private UserEntity createdBy;

    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "updated_by", referencedColumnName = "id")
    private UserEntity updatedBy;

    @Column(name = "status", nullable = false)
    @ColumnDefault("true")
    private Boolean status;

    @Column(name = "deactivate", nullable = false)
    @ColumnDefault("false")
    private Boolean deactivate;


    @PrePersist
    public void beforePersist() {
        this.createdBy = new UserEntity(1L);
        this.updatedBy = new UserEntity(1L);
    }

    @PreUpdate
    public void beforeUpdate() {
        this.createdBy = new UserEntity(1L);
        this.updatedBy = new UserEntity(1L);
    }

    @PreRemove
    public void BeforeRemove() {
        this.createdBy = new UserEntity(1L);
        this.updatedBy = new UserEntity(1L);
    }


}