package com.example.crm_service.services;

import com.example.crm_service.domain.Proposal;
import com.example.crm_service.domain.ProposalStatus;
import com.example.crm_service.messaging.dto.ContractSignedEventDTO;
import com.example.crm_service.repositories.ProposalRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ProposalSignatureService {

    private final ProposalRepository proposalRepository;

    public ProposalSignatureService(ProposalRepository proposalRepository) {
        this.proposalRepository = proposalRepository;
    }

    @Transactional
    public void handleContractSignedEvent(ContractSignedEventDTO event) {
        if (event == null || event.getContractUuid() == null || event.getContractUuid().isBlank())
            return;

        if (!ProposalStatus.SIGNED.name().equalsIgnoreCase(event.getStatus()))
            return;

        Proposal proposal = proposalRepository.findByContractUuid(event.getContractUuid());
        if (proposal == null)
            return;

        if (!proposal.getStatus().equals(ProposalStatus.SIGNED)) {
            proposal.setStatus(ProposalStatus.SIGNED);
            proposalRepository.save(proposal);
        }
    }

}
