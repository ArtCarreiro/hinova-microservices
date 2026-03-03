package com.example.crm_service.services;

import com.example.crm_service.domain.Proposal;
import com.example.crm_service.domain.ProposalItem;
import com.example.crm_service.domain.ProposalStatus;
import com.example.crm_service.dto.SendToSignatureResponseDTO;
import com.example.crm_service.integration.SignClient;
import com.example.crm_service.integration.dto.SignCreateContractResponseDTO;
import com.example.crm_service.repositories.ProposalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProposalServiceTest {

    @Mock
    private ProposalRepository proposalRepository;

    @Mock
    private SignClient signClient;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProposalService proposalService;

    @Test
    void createProposalShouldReturnExistingWhenIdempotencyKeyAlreadyExists() {
        Proposal existing = new Proposal();
        existing.setUuid("proposal-1");
        when(proposalRepository.findByIdempotencyKey("idem-1")).thenReturn(existing);

        Proposal payload = new Proposal();
        payload.setUuid("payload-uuid");

        Proposal result = proposalService.createProposal("idem-1", payload);

        assertThat(result).isSameAs(existing);
        verify(proposalRepository, never()).save(any(Proposal.class));
    }

    @Test
    void createProposalShouldNormalizePayloadAndPersistWhenFirstRequest() {
        ProposalItem item = new ProposalItem();
        item.setName("Plano");
        item.setQuantity(1);
        item.setUnitPrice(100.0);

        Proposal payload = new Proposal();
        payload.setUuid("payload-uuid");
        payload.setStatus(ProposalStatus.SIGNED);
        payload.setContractUuid("contract-from-client");
        payload.setItems(List.of(item));

        when(proposalRepository.findByIdempotencyKey("idem-2")).thenReturn(null);
        when(proposalRepository.save(payload)).thenReturn(payload);

        Proposal result = proposalService.createProposal("idem-2", payload);

        assertThat(result).isSameAs(payload);
        assertThat(payload.getUuid()).isNull();
        assertThat(payload.getStatus()).isEqualTo(ProposalStatus.DRAFT);
        assertThat(payload.getContractUuid()).isNull();
        assertThat(payload.getIdempotencyKey()).isEqualTo("idem-2");
        assertThat(payload.getItems().get(0).getProposal()).isSameAs(payload);
        verify(proposalRepository).save(payload);
    }

    @Test
    void createProposalShouldHandleConcurrentRequestsWithSameIdempotencyKey() {
        Proposal persisted = new Proposal();
        persisted.setUuid("proposal-2");

        Proposal payload = new Proposal();
        when(proposalRepository.findByIdempotencyKey("idem-race")).thenReturn(null, persisted);
        when(proposalRepository.save(payload)).thenThrow(new DataIntegrityViolationException("duplicate"));

        Proposal result = proposalService.createProposal("idem-race", payload);

        assertThat(result).isSameAs(persisted);
        verify(proposalRepository).save(payload);
        verify(proposalRepository, times(2)).findByIdempotencyKey("idem-race");
    }

    @Test
    void sendToSignatureShouldBeIdempotentWhenContractAlreadyExists() {
        Proposal proposal = new Proposal();
        proposal.setUuid("proposal-3");
        proposal.setStatus(ProposalStatus.SENT_TO_SIGNATURE);
        proposal.setContractUuid("contract-3");

        SendToSignatureResponseDTO result = proposalService.sendToSignature(proposal);

        assertThat(result.getProposalUuid()).isEqualTo("proposal-3");
        assertThat(result.getStatus()).isEqualTo(ProposalStatus.SENT_TO_SIGNATURE.name());
        assertThat(result.getContractUuid()).isEqualTo("contract-3");
        verifyNoInteractions(signClient, modelMapper, proposalRepository);
    }

    @Test
    void sendToSignatureShouldCreateContractAndPersistStatus() {
        Proposal proposal = new Proposal();
        proposal.setUuid("proposal-4");
        proposal.setStatus(ProposalStatus.DRAFT);

        SignCreateContractResponseDTO signResponse = new SignCreateContractResponseDTO();
        signResponse.setUuid("contract-4");
        signResponse.setStatus("PENDING_SIGNATURE");

        SendToSignatureResponseDTO mapped = new SendToSignatureResponseDTO("proposal-4", "SENT_TO_SIGNATURE", "contract-4");

        when(signClient.createContract(any())).thenReturn(signResponse);
        when(proposalRepository.save(proposal)).thenReturn(proposal);
        when(modelMapper.map(eq(proposal), eq(SendToSignatureResponseDTO.class))).thenReturn(mapped);

        SendToSignatureResponseDTO result = proposalService.sendToSignature(proposal);

        assertThat(proposal.getStatus()).isEqualTo(ProposalStatus.SENT_TO_SIGNATURE);
        assertThat(proposal.getContractUuid()).isEqualTo("contract-4");
        assertThat(result).isSameAs(mapped);
        verify(proposalRepository).save(proposal);
    }
}
