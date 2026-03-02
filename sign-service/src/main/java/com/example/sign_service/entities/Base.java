package com.example.sign_service.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Data
@EqualsAndHashCode
@MappedSuperclass
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
