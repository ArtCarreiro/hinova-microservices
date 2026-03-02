package com.example.crm_service.controllers.responseDTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ContractSignedCallbackRequestDTO {

    String contractUuid;
    String externalProposalId;
    LocalDateTime signedAt;

}
