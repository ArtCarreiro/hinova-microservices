package com.example.crm_service.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "proposal_items")
public class ProposalItem extends Base {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposal_uuid", nullable = false)
    private Proposal proposal;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double unitPrice;

}
