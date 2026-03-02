package com.example.sign_service.integration;

import com.example.sign_service.integration.dto.ContractSignedCallbackRequest;
import com.example.sign_service.integration.interfaces.CrmCallbackHttpClientBO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class CrmCallbackHttpClient implements CrmCallbackHttpClientBO {

    private final WebClient webClient;

    public CrmCallbackHttpClient(WebClient.Builder builder,
                                 @Value("${crm.base-url}") String signUrl) {
        this.webClient = builder.baseUrl(signUrl).build();
    }

    @Override
    // Envia uma solicitação para notificar o CRM sobre um contrato assinado
    public void notifyContractSigned(ContractSignedCallbackRequest request) {
        webClient.post()
                .uri("/sign/callbacks/contract-signed")
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
