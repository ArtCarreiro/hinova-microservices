package com.example.sign_service.controllers.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractRequestDTO {

    @NotBlank
    private String externalProposalId;

    @NotBlank
    private String callbackUrl;

}
