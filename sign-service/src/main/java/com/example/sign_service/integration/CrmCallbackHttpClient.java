package com.example.sign_service.integration;

import com.example.sign_service.integration.dto.ContractSignedCallbackPayload;
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
    public void notifyContractSigned(ContractSignedCallbackPayload payload) {
        webClient.post()
                .uri("/sign/callbacks/contract-signed")
                .bodyValue(payload)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
