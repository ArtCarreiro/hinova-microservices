package com.example.crm_service.services;

import com.example.crm_service.domain.Proposal;
import com.example.crm_service.domain.ProposalStatus;
import com.example.crm_service.dto.ContractSignedCallbackRequestDTO;
import com.example.crm_service.repositories.ProposalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CallbackServiceTest {

    @Mock
    private ProposalRepository proposalRepository;

    @InjectMocks
    private CallbackService callbackService;

    @Test
    void handleContractSignedCallbackShouldReturnNotFoundWhenProposalDoesNotExist() {
        ContractSignedCallbackRequestDTO request = new ContractSignedCallbackRequestDTO();
        request.setContractUuid("missing-contract");
        request.setStatus("SIGNED");

        when(proposalRepository.findByContractUuid("missing-contract")).thenReturn(null);

        assertThatThrownBy(() -> callbackService.handleContractSignedCallback(request))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> {
                    ResponseStatusException error = (ResponseStatusException) ex;
                    assertThat(error.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                });
    }

    @Test
    void handleContractSignedCallbackShouldBeIdempotentWhenProposalAlreadySigned() {
        ContractSignedCallbackRequestDTO request = new ContractSignedCallbackRequestDTO();
        request.setContractUuid("contract-1");
        request.setStatus("SIGNED");

        Proposal proposal = new Proposal();
        proposal.setContractUuid("contract-1");
        proposal.setStatus(ProposalStatus.SIGNED);

        when(proposalRepository.findByContractUuid("contract-1")).thenReturn(proposal);

        callbackService.handleContractSignedCallback(request);

        verify(proposalRepository, never()).save(any(Proposal.class));
    }

    @Test
    void handleContractSignedCallbackShouldUpdateStatusWhenProposalIsNotSigned() {
        ContractSignedCallbackRequestDTO request = new ContractSignedCallbackRequestDTO();
        request.setContractUuid("contract-2");
        request.setStatus("SIGNED");

        Proposal proposal = new Proposal();
        proposal.setContractUuid("contract-2");
        proposal.setStatus(ProposalStatus.SENT_TO_SIGNATURE);

        when(proposalRepository.findByContractUuid("contract-2")).thenReturn(proposal);
        when(proposalRepository.save(proposal)).thenReturn(proposal);

        callbackService.handleContractSignedCallback(request);

        assertThat(proposal.getStatus()).isEqualTo(ProposalStatus.SIGNED);
        verify(proposalRepository).save(proposal);
    }

    @Test
    void verifyContractSignedCallbackShouldRejectUnexpectedStatus() {
        ContractSignedCallbackRequestDTO request = new ContractSignedCallbackRequestDTO();
        request.setContractUuid("contract-3");
        request.setStatus("PENDING_SIGNATURE");

        Proposal proposal = new Proposal();
        proposal.setUuid("proposal-3");
        proposal.setContractUuid("contract-3");
        proposal.setStatus(ProposalStatus.SENT_TO_SIGNATURE);

        assertThatThrownBy(() -> callbackService.verifyContractSignedCallback(proposal, request))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> {
                    ResponseStatusException error = (ResponseStatusException) ex;
                    assertThat(error.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                });
    }
}
