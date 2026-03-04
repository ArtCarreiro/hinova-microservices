package com.example.crm_service.messaging.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractSignedEventDTO {

    private String contractUuid;
    private String status;

}
