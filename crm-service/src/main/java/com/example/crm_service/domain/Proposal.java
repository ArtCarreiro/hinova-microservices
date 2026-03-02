package com.example.crm_service.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "proposals")
public class Proposal extends Base {

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String customerEmail;

    @Column(nullable = false)
    private String customerCompany;

    private String contractUuid;

    @OneToMany(mappedBy = "proposal", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ProposalItem> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ProposalStatus status;


}
