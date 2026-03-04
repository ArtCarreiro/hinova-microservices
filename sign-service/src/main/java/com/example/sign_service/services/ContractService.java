package com.example.sign_service.services;

import com.example.sign_service.dto.ContractRequestDTO;
import com.example.sign_service.dto.ContractResponseDTO;
import com.example.sign_service.domains.Contract;
import com.example.sign_service.domains.ContractStatus;
import com.example.sign_service.dto.CreateDocumentDTO;
import com.example.sign_service.messaging.ContractSignedEvent;
import com.example.sign_service.repositories.ContractRepository;
import com.example.sign_service.services.interfaces.ContractServiceBO;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private ContractSignedEvent contractSignedEvent;

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
        newContract.setStatus(ContractStatus.PENDING_SIGNATURE);

        String json = convertToJson(modelMapper.map(newContract, CreateDocumentDTO.class));
        if (json != null)
            newContract.setDocumentJson(json);
        Contract saved = contractRepository.save(newContract);

        ContractResponseDTO response = new ContractResponseDTO();
        response.setUuid(saved.getUuid());
        response.setStatus(saved.getStatus().name());

        return response;
    }

    @Override
    @Transactional
    public void signContract(Contract contract) {
        if (contract.getStatus().equals(ContractStatus.SIGNED))
            return;

        contract.setStatus(ContractStatus.SIGNED);
        contract.setSignedAt(Date.from(Instant.now()));
        contractRepository.save(contract);
        contractSignedEvent.publishSigned(contract);
    }

    // Converte o contrato para JSON
    public String convertToJson(CreateDocumentDTO contract) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(contract);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter o contrato para JSON: " + e.getMessage());
        }
    }

}
