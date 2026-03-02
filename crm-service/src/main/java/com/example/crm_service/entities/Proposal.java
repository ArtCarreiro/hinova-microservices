package com.example.crm_service.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
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
    @Column(nullable = false)
    private ProposalStatus status;


}
