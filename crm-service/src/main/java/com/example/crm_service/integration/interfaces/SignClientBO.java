package com.example.crm_service.integration.interfaces;

import com.example.crm_service.integration.dto.SignCreateContractRequestDTO;
import com.example.crm_service.integration.dto.SignCreateContractResponseDTO;

public interface SignClientBO {

    SignCreateContractResponseDTO createContract(SignCreateContractRequestDTO request);

}
