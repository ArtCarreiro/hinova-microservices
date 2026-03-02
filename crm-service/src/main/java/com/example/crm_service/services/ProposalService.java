package com.example.crm_service.services;

import com.example.crm_service.dto.SendToSignatureResponseDTO;
import com.example.crm_service.domain.Proposal;
import com.example.crm_service.domain.ProposalStatus;
import com.example.crm_service.integration.SignClient;
import com.example.crm_service.integration.dto.SignCreateContractRequestDTO;
import com.example.crm_service.integration.dto.SignCreateContractResponseDTO;
import com.example.crm_service.repositories.ProposalRepository;
import com.example.crm_service.services.interfaces.ProposalServiceBO;
import jakarta.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProposalService implements ProposalServiceBO {

    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    private SignClient signClient;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public Proposal createProposal(Proposal newProposal) {
        if (newProposal.getItems() != null)
            newProposal.getItems().forEach(item -> item.setProposal(newProposal));
        return proposalRepository.save(newProposal);
    }

    @Override
    public SendToSignatureResponseDTO sendToSignature(Proposal proposal) {
        try {

            // Caso já tenha sido enviado para assinatura, devolve o estado atual.
            if (proposal.getContractUuid() != null && !proposal.getContractUuid().isBlank()) {
                return new SendToSignatureResponseDTO(
                        proposal.getUuid(),
                        proposal.getStatus().name(),
                        proposal.getContractUuid()
                );
            }

            SignCreateContractRequestDTO signRequest = new SignCreateContractRequestDTO();
            signRequest.setExternalProposalId(proposal.getUuid());
            SignCreateContractResponseDTO signResponse = signClient.createContract(signRequest);

            proposal.setStatus(ProposalStatus.SENT_TO_SIGNATURE);
            proposal.setContractUuid(signResponse.getUuid());
            proposalRepository.save(proposal);
            return modelMapper.map(proposal, SendToSignatureResponseDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar proposta para assinatura", e);
        }

    }
}
