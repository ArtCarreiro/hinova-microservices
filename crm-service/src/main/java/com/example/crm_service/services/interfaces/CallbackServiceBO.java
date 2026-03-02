package com.example.crm_service.services.interfaces;

import com.example.crm_service.controllers.dto.ContractSignedCallbackRequestDTO;

public interface CallbackServiceBO {

    void handleContractSignedCallback(ContractSignedCallbackRequestDTO request);

}
