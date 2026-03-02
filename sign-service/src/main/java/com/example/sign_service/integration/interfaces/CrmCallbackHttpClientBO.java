package com.example.sign_service.integration.interfaces;

import com.example.sign_service.integration.dto.ContractSignedCallbackRequest;

public interface CrmCallbackHttpClientBO {
    void notifyContractSigned(ContractSignedCallbackRequest payload);
}
