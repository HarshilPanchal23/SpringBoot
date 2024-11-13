package com.example.manyToOne.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    private UserEntity createdBy;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
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
        this.createdBy = new UserEntity(1L); // Ensure that this ID exists in the database
        this.updatedBy = new UserEntity(1L); // Ensure that this ID exists in the database
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