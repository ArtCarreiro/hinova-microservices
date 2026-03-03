package com.example.crm_service.services;

import com.example.crm_service.dto.ContractSignedCallbackRequestDTO;
import com.example.crm_service.domain.Proposal;
import com.example.crm_service.domain.ProposalStatus;
import com.example.crm_service.repositories.ProposalRepository;
import com.example.crm_service.services.interfaces.CallbackServiceBO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CallbackService implements CallbackServiceBO {

    @Autowired
    private ProposalRepository proposalRepository;

    // Trata o callback de contrato assinado e atualiza o status da proposta
    @Override
    @Transactional
    public void handleContractSignedCallback(ContractSignedCallbackRequestDTO request) {
        Proposal proposal = proposalRepository.findByContractUuid(request.getContractUuid());
        if (proposal == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nao existe proposta para o contractUuid informado: " + request.getContractUuid());
        verifyContractSignedCallback(proposal, request);
        if (!proposal.getStatus().equals(ProposalStatus.SIGNED)) {
            proposal.setStatus(ProposalStatus.SIGNED);
            proposalRepository.save(proposal);
        }
    }

    // Repetição do callback para proposta já assinada, não refaz a assinatura.
    public void verifyContractSignedCallback(Proposal proposal, ContractSignedCallbackRequestDTO request) {
        if (!ProposalStatus.SIGNED.name().equalsIgnoreCase(request.getStatus()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status de callback invalido: " + request.getStatus());

        if (proposal.getContractUuid() == null || !proposal.getContractUuid().equals(request.getContractUuid()))
            throw new ResponseStatusException( HttpStatus.CONFLICT,
                    "Incompatibilidade de UUID do contrato para a proposta " + proposal.getUuid());
    }

}
