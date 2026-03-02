package com.example.sign_service.domains;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "contracts")
public class Contract extends Base {

    @Column(nullable = false, unique = true)
    private String externalProposalId;

    @Column(name = "document_json", nullable = false, columnDefinition = "TEXT")
    private String documentJson;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "signed_at")
    private Date signedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ContractStatus status;

}
