package com.example.sign_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDocumentDTO {

    private String externalProposalId;

    public String getDescription() {
        return """
                Documento gerado automaticamente pela plataforma SIGN.
                Este contrato foi criado a partir da proposta enviada pelo módulo CRM.
                Chave de identificação da proposta: %s
                O módulo SIGN é responsável por gerenciar o ciclo de assinatura
                eletrônica e notificar o CRM após a conclusão do processo.
                """.formatted(externalProposalId != null ? externalProposalId : "N/A");
    }
}