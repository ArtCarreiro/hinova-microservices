package com.example.sign_service.integration.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractSignedCallbackRequest {

    String contractUuid;
    String status;

}
