package com.example.sign_service.services.interfaces;

import com.example.sign_service.dto.ContractRequestDTO;
import com.example.sign_service.dto.ContractResponseDTO;
import com.example.sign_service.domains.Contract;

public interface ContractServiceBO {

    void signContract(Contract contract);
    ContractResponseDTO createContract(ContractRequestDTO contract);

}
