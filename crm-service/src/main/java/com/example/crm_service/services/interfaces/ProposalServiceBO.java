package com.example.crm_service.services.interfaces;

import com.example.crm_service.controllers.responseDTO.SendToSignatureResponseDTO;
import com.example.crm_service.entities.Proposal;

public interface ProposalServiceBO {

    Proposal createProposal(Proposal newProposal);
    SendToSignatureResponseDTO sendToSignature(Proposal proposal);

}
