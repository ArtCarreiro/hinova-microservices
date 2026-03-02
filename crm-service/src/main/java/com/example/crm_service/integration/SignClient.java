package com.example.crm_service.integration;

import com.example.crm_service.integration.dto.SignCreateContractRequestDTO;
import com.example.crm_service.integration.dto.SignCreateContractResponseDTO;
import com.example.crm_service.integration.interfaces.SignClientBO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class SignClient implements SignClientBO {

    private final WebClient webClient;

    public SignClient(WebClient.Builder builder,
                      @Value("${sign.base-url}") String signUrl) {
        this.webClient = builder.baseUrl(signUrl).build();
    }

    @Override
    public SignCreateContractResponseDTO createContract(SignCreateContractRequestDTO request) {
        return webClient.post()
                .uri("/contracts")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(SignCreateContractResponseDTO.class)
                .block();
    }
}


