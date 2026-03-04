package com.example.sign_service.services;

import com.example.sign_service.dto.ContractRequestDTO;
import com.example.sign_service.dto.ContractResponseDTO;
import com.example.sign_service.domains.Contract;
import com.example.sign_service.domains.ContractStatus;
import com.example.sign_service.dto.CreateDocumentDTO;
import com.example.sign_service.integration.CrmCallbackHttpClient;
import com.example.sign_service.integration.dto.ContractSignedCallbackRequest;
import com.example.sign_service.repositories.ContractRepository;
import com.example.sign_service.services.interfaces.ContractServiceBO;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.Date;

@Service
public class ContractService implements ContractServiceBO {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    CrmCallbackHttpClient crmCallbackHttpClient;

    @Autowired
    ModelMapper modelMapper;

    @Override
    @Transactional
    public ContractResponseDTO createContract(ContractRequestDTO request) {
        Contract existing = contractRepository
                .findByExternalProposalId(request.getExternalProposalId());

        if (existing != null)
            return toContractResponse(existing);

        Contract newContract = new Contract();
        newContract.setExternalProposalId(request.getExternalProposalId());
        newContract.setStatus(ContractStatus.PENDING_SIGNATURE);

        String json = convertToJson(modelMapper.map(newContract, CreateDocumentDTO.class));
        if (json != null)
            newContract.setDocumentJson(json);
        try {
            Contract saved = contractRepository.save(newContract);
            return toContractResponse(saved);
        } catch (DataIntegrityViolationException e) {
            Contract contract = contractRepository.findByExternalProposalId(request.getExternalProposalId());
            if (contract != null)
                return toContractResponse(contract);
            throw e;
        }
    }

    @Override
    @Transactional
    public void signContract(Contract contract) {
        if (contract.getStatus().equals(ContractStatus.SIGNED))
            return;

        contract.setStatus(ContractStatus.SIGNED);
        contract.setSignedAt(Date.from(Instant.now()));
        contractRepository.save(contract);

        ContractSignedCallbackRequest request = new ContractSignedCallbackRequest();
        request.setContractUuid(contract.getUuid());
        request.setStatus(contract.getStatus().name());
        crmCallbackHttpClient.notifyContractSigned(request);
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

    private ContractResponseDTO toContractResponse(Contract contract) {
        ContractResponseDTO response = new ContractResponseDTO();
        response.setUuid(contract.getUuid());
        response.setStatus(contract.getStatus().name());
        return response;
    }

}
