package com.example.sign_service.entities;

import jakarta.persistence.*;

import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "contracts")
public class Contract extends Base {

    @Column(nullable = false, unique = true)
    private String externalProposalId;

    @Column(nullable = false)
    private String document;

    @Column(updatable = false)
    private Date signedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContractStatus status;

}
