package com.example.sign_service.integration.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ContractSignedCallbackPayload {

    String contractUuid;
    String externalProposalId;
    LocalDateTime signedAt;

}
