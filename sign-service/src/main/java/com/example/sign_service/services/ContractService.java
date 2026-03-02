package com.example.sign_service.services;

import com.example.sign_service.entities.Contract;
import com.example.sign_service.entities.ContractStatus;
import com.example.sign_service.repositories.ContractRepository;
import com.example.sign_service.services.interfaces.ContractServiceBO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class ContractService implements ContractServiceBO {

    @Autowired
    private ContractRepository contractRepository;

    @Override
    public Contract createContract(Contract contract) {
        verifyContractStatus(contract);
        try {
            contract.setStatus(ContractStatus.PENDING_SIGNATURE);
            contractRepository.save(contract);
            return contract;
        } catch (Exception e) {
            throw new RuntimeException("Error creating contract: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Contract signContract(Contract contract) {
        verifyContractStatus(contract);
        try {
            contract.setStatus(ContractStatus.SIGNED);
            contract.setSignedAt(Date.from(Instant.now()));
            contractRepository.save(contract);
            return contract;
        } catch (Exception e) {
            throw new RuntimeException("Error signing contract: " + e.getMessage());
        }
    }

    public void verifyContractStatus(Contract contract) {
        if (contract.getStatus().equals(ContractStatus.SIGNED))
            throw new IllegalStateException("Contract already signed");
        if (contract.getStatus().equals(ContractStatus.PENDING_SIGNATURE))
            throw new IllegalStateException("Contract already pending signature");
    }
}
