package com.example.sign_service.messaging;

import com.example.sign_service.domains.Contract;
import com.example.sign_service.messaging.dto.ContractSignedEventDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ContractSignedEvent {

    private final KafkaTemplate<String, ContractSignedEventDTO> kafkaTemplate;
    private final String contractSignedTopic;

    public ContractSignedEvent(
            KafkaTemplate<String, ContractSignedEventDTO> kafkaTemplate,
            @Value("${app.kafka.topics.contract-signed}") String contractSignedTopic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.contractSignedTopic = contractSignedTopic;
    }

    public void publishSigned(Contract contract) {
        ContractSignedEventDTO event = new ContractSignedEventDTO();
        event.setContractUuid(contract.getUuid());
        event.setStatus(contract.getStatus().name());
        kafkaTemplate.send(contractSignedTopic, contract.getUuid(), event);
    }

}
