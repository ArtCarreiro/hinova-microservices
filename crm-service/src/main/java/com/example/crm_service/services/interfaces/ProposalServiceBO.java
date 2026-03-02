package com.example.crm_service.services.interfaces;

import com.example.crm_service.controllers.dto.SendToSignatureResponseDTO;
import com.example.crm_service.domain.Proposal;

public interface ProposalServiceBO {

    Proposal createProposal(Proposal newProposal);
    SendToSignatureResponseDTO sendToSignature(Proposal proposal);

}
