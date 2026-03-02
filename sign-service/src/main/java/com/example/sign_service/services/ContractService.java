package com.example.sign_service.services;

import com.example.sign_service.controllers.dto.ContractRequestDTO;
import com.example.sign_service.controllers.dto.ContractResponseDTO;
import com.example.sign_service.entities.Contract;
import com.example.sign_service.entities.ContractStatus;
import com.example.sign_service.repositories.ContractRepository;
import com.example.sign_service.services.interfaces.ContractServiceBO;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class ContractService implements ContractServiceBO {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public ContractResponseDTO createContract(ContractRequestDTO request) {
        Contract existing = contractRepository
                .findByExternalProposalId(request.getExternalProposalId());

        if (existing != null) {
            ContractResponseDTO response = new ContractResponseDTO();
            response.setUuid(existing.getUuid());
            response.setStatus(existing.getStatus().name());
            return response;
        }

        Contract newContract = new Contract();
        newContract.setExternalProposalId(request.getExternalProposalId());
        newContract.setCallbackUrl(request.getCallbackUrl()); // se você adicionou esse campo
        newContract.setStatus(ContractStatus.PENDING_SIGNATURE);
        newContract.setDocument("Contrato simulado da proposta: " + request.getExternalProposalId());

        Contract saved = contractRepository.save(newContract);

        ContractResponseDTO response = new ContractResponseDTO();
        response.setUuid(saved.getUuid());
        response.setStatus(saved.getStatus().name());

        return response;
    }

    @Override
    @Transactional
    public void signContract(Contract contract) {
        verifyContractStatus(contract);
        try {
            contract.setStatus(ContractStatus.SIGNED);
            contract.setSignedAt(Date.from(Instant.now()));
            contractRepository.save(contract);
        } catch (Exception e) {
            throw new RuntimeException("Error signing contract: " + e.getMessage());
        }
    }

    public void verifyContractStatus(Contract contract) {
        if (contract.getStatus().equals(ContractStatus.SIGNED))
            throw new IllegalStateException("Contract already signed");
    }
}
