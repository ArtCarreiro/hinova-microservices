package com.example.crm_service.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "proposals")
public class Proposal extends Base {

    @NotBlank
    @Column(nullable = false)
    private String customerName;

    @Email
    @NotBlank
    @Column(nullable = false)
    private String customerEmail;

    @NotBlank
    @Column(nullable = false)
    private String customerCompany;

    private String contractUuid;

    @JsonIgnore
    @Column(unique = true, length = 100)
    private String idempotencyKey;

    @Valid
    @NotEmpty
    @OneToMany(mappedBy = "proposal", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ProposalItem> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ProposalStatus status = ProposalStatus.DRAFT;


}
