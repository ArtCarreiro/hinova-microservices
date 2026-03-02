package com.example.crm_service.controllers.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractSignedCallbackRequestDTO {

    String contractUuid;
    String status;

}
