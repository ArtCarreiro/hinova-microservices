package com.example.sign_service.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Base {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    @CreatedDate
    @Column(updatable = false)
    private Date created;

    private Boolean active = true;

    private Boolean deleted = false;

}
