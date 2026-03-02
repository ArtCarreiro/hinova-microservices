package com.example.sign_service.integration.interfaces;

import com.example.sign_service.integration.dto.ContractSignedCallbackPayload;

public interface CrmCallbackHttpClientBO {
    void notifyContractSigned(ContractSignedCallbackPayload payload);
}
