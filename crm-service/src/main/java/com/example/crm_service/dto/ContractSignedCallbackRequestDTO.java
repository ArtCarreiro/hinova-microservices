package com.example.crm_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractSignedCallbackRequestDTO {

    @NotBlank
    private String contractUuid;

    @NotBlank
    private String status;

}
