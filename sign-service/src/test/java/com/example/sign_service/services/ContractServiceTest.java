package com.example.sign_service.services;

import com.example.sign_service.domains.Contract;
import com.example.sign_service.domains.ContractStatus;
import com.example.sign_service.dto.ContractRequestDTO;
import com.example.sign_service.dto.ContractResponseDTO;
import com.example.sign_service.dto.CreateDocumentDTO;
import com.example.sign_service.messaging.ContractSignedEvent;
import com.example.sign_service.repositories.ContractRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractServiceTest {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private ContractSignedEvent contractSignedEvent;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ContractService contractService;

    @Test
    void createContractShouldReturnExistingWhenExternalProposalIdAlreadyExists() {
        ContractRequestDTO request = new ContractRequestDTO();
        request.setExternalProposalId("proposal-1");

        Contract existing = new Contract();
        existing.setUuid("contract-1");
        existing.setStatus(ContractStatus.PENDING_SIGNATURE);

        when(contractRepository.findByExternalProposalId("proposal-1")).thenReturn(existing);

        ContractResponseDTO response = contractService.createContract(request);

        assertThat(response.getUuid()).isEqualTo("contract-1");
        assertThat(response.getStatus()).isEqualTo(ContractStatus.PENDING_SIGNATURE.name());
        verify(contractRepository, never()).save(any(Contract.class));
    }

    @Test
    void signContractShouldBeIdempotentWhenAlreadySigned() {
        Contract contract = new Contract();
        contract.setStatus(ContractStatus.SIGNED);
        contract.setUuid("contract-2");

        contractService.signContract(contract);

        verify(contractRepository, never()).save(any(Contract.class));
        verifyNoInteractions(contractSignedEvent);
    }

    @Test
    void signContractShouldSignAndPublishEvent() {
        Contract contract = new Contract();
        contract.setUuid("contract-3");
        contract.setStatus(ContractStatus.PENDING_SIGNATURE);

        contractService.signContract(contract);

        assertThat(contract.getStatus()).isEqualTo(ContractStatus.SIGNED);
        assertThat(contract.getSignedAt()).isNotNull();
        verify(contractRepository).save(contract);
        verify(contractSignedEvent).publishSigned(contract);
    }

    @Test
    void createContractShouldPersistNewContractWhenNotFound() {
        ContractRequestDTO request = new ContractRequestDTO();
        request.setExternalProposalId("proposal-4");

        Contract persisted = new Contract();
        persisted.setUuid("contract-4");
        persisted.setStatus(ContractStatus.PENDING_SIGNATURE);

        when(contractRepository.findByExternalProposalId("proposal-4")).thenReturn(null);
        when(modelMapper.map(any(Contract.class), eq(CreateDocumentDTO.class))).thenReturn(new CreateDocumentDTO());
        when(contractRepository.save(any(Contract.class))).thenReturn(persisted);

        ContractResponseDTO response = contractService.createContract(request);

        assertThat(response.getUuid()).isEqualTo("contract-4");
        assertThat(response.getStatus()).isEqualTo("PENDING_SIGNATURE");
        verify(contractRepository).save(any(Contract.class));
    }
}
