package com.example.crm_service.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "proposal_items")
public class ProposalItem extends Base {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposal_uuid", nullable = false)
    @JsonIgnore
    private Proposal proposal;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double unitPrice;

}
