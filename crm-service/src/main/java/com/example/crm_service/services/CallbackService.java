package com.example.crm_service.services;

import com.example.crm_service.dto.ContractSignedCallbackRequestDTO;
import com.example.crm_service.domain.Proposal;
import com.example.crm_service.domain.ProposalStatus;
import com.example.crm_service.repositories.ProposalRepository;
import com.example.crm_service.services.interfaces.CallbackServiceBO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CallbackService implements CallbackServiceBO {

    @Autowired
    private ProposalRepository proposalRepository;

    // Trata o callback de contrato assinado e atualiza o status da proposta
    @Override
    @Transactional
    public void handleContractSignedCallback(ContractSignedCallbackRequestDTO request) {
        try {
            Proposal proposal = proposalRepository.findByContractUuid(request.getContractUuid());
            verifyContractSignedCallback(proposal, request);
            proposal.setStatus(ProposalStatus.SIGNED);
            proposalRepository.save(proposal);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // Verifica se a proposta já está assinada ou se o UUID do contrato é incompatível
    public void verifyContractSignedCallback(Proposal proposal, ContractSignedCallbackRequestDTO request) {
        if (proposal.getStatus().equals(ProposalStatus.SIGNED))
            throw new RuntimeException("Contrato já assinado.");
        if (proposal.getContractUuid() == null || !proposal.getContractUuid().equals(request.getContractUuid()))
            throw new IllegalStateException("Incompatibilidade de UUID do contrato para a proposta " + proposal.getUuid());
    }

}
