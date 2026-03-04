package com.example.crm_service.messaging;

import com.example.crm_service.messaging.dto.ContractSignedEventDTO;
import com.example.crm_service.services.ProposalSignatureService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ContractSignedEvent {

    private final ProposalSignatureService proposalSignatureService;

    public ContractSignedEvent(ProposalSignatureService proposalSignatureService) {
        this.proposalSignatureService = proposalSignatureService;
    }

    @KafkaListener(topics = "${app.kafka.topics.contract-signed}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeSignedEvent(ContractSignedEventDTO event) {
        proposalSignatureService.handleContractSignedEvent(event);
    }

}
