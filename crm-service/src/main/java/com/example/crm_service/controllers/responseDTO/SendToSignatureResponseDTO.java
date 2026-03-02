package com.example.crm_service.controllers.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendToSignatureResponseDTO {

    String proposalUuid;
    String status;
    String contractUuid;

}
