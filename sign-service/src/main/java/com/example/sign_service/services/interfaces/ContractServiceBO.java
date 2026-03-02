package com.example.sign_service.services.interfaces;

import com.example.sign_service.controllers.dto.ContractRequestDTO;
import com.example.sign_service.controllers.dto.ContractResponseDTO;
import com.example.sign_service.entities.Contract;

public interface ContractServiceBO {

    void signContract(Contract contract);
    ContractResponseDTO createContract(ContractRequestDTO contract);
}
