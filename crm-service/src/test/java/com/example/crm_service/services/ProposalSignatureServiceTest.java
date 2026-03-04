package com.example.crm_service.services;

import com.example.crm_service.domain.Proposal;
import com.example.crm_service.domain.ProposalStatus;
import com.example.crm_service.messaging.dto.ContractSignedEventDTO;
import com.example.crm_service.repositories.ProposalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProposalSignatureServiceTest {

    @Mock
    private ProposalRepository proposalRepository;

    @InjectMocks
    private ProposalSignatureService proposalSignatureService;

    @Test
    void handleContractSignedEventShouldIgnoreInvalidPayload() {
        ContractSignedEventDTO invalid = new ContractSignedEventDTO();
        invalid.setStatus("SIGNED");

        proposalSignatureService.handleContractSignedEvent(invalid);

        verifyNoInteractions(proposalRepository);
    }

    @Test
    void handleContractSignedEventShouldIgnoreUnexpectedStatus() {
        ContractSignedEventDTO event = new ContractSignedEventDTO();
        event.setContractUuid("contract-1");
        event.setStatus("PENDING_SIGNATURE");

        proposalSignatureService.handleContractSignedEvent(event);

        verifyNoInteractions(proposalRepository);
    }

    @Test
    void handleContractSignedEventShouldBeIdempotentWhenProposalAlreadySigned() {
        ContractSignedEventDTO event = new ContractSignedEventDTO();
        event.setContractUuid("contract-2");
        event.setStatus("SIGNED");

        Proposal proposal = new Proposal();
        proposal.setContractUuid("contract-2");
        proposal.setStatus(ProposalStatus.SIGNED);
        when(proposalRepository.findByContractUuid("contract-2")).thenReturn(proposal);

        proposalSignatureService.handleContractSignedEvent(event);

        verify(proposalRepository).findByContractUuid("contract-2");
        verify(proposalRepository, never()).save(any(Proposal.class));
    }

    @Test
    void handleContractSignedEventShouldUpdateStatusWhenProposalIsNotSigned() {
        ContractSignedEventDTO event = new ContractSignedEventDTO();
        event.setContractUuid("contract-3");
        event.setStatus("SIGNED");

        Proposal proposal = new Proposal();
        proposal.setContractUuid("contract-3");
        proposal.setStatus(ProposalStatus.SENT_TO_SIGNATURE);
        when(proposalRepository.findByContractUuid("contract-3")).thenReturn(proposal);
        when(proposalRepository.save(proposal)).thenReturn(proposal);

        proposalSignatureService.handleContractSignedEvent(event);

        assertThat(proposal.getStatus()).isEqualTo(ProposalStatus.SIGNED);
        verify(proposalRepository).save(proposal);
    }

}
