package com.example.crm_service.controllers;

import com.example.crm_service.dto.SendToSignatureResponseDTO;
import com.example.crm_service.domain.Proposal;
import com.example.crm_service.repositories.ProposalRepository;
import com.example.crm_service.services.interfaces.ProposalServiceBO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/proposals")
@Tag(name = "Proposals", description = "Operacoes de propostas no CRM")
@Validated
public class ProposalController {

    @Autowired
    private ProposalServiceBO proposalServiceBO;

    @Autowired
    private ProposalRepository proposalRepository;

    @PostMapping
    @Operation(summary = "Criar proposta", description = "Cria uma nova proposta comercial no CRM.")
    public ResponseEntity<Proposal> createProposal(
            @RequestHeader("Idempotency-Key") @NotBlank String idempotencyKey,
            @Valid @RequestBody Proposal newProposal
    ) {
        Proposal proposal = proposalServiceBO.createProposal(idempotencyKey, newProposal);
        return proposal != null ? ResponseEntity.ok(proposal) : ResponseEntity.badRequest().build();
    }

    @GetMapping("/{uuid}")
    @Operation(summary = "Consultar proposta", description = "Retorna os dados de uma proposta pelo UUID.")
    public ResponseEntity<Proposal> getProposal(@PathVariable String uuid) {
        Proposal proposal = proposalRepository.findByUuid(uuid);
        return proposal != null ? ResponseEntity.ok(proposal) : ResponseEntity.notFound().build();
    }

    @PostMapping("/{uuid}/send-to-signature")
    @Operation(summary = "Enviar proposta para assinatura", description = "Solicita ao SIGN a geracao de contrato para a proposta.")
    public ResponseEntity<SendToSignatureResponseDTO> sendToSignature(@PathVariable String uuid) {
        Proposal proposal = proposalRepository.findByUuid(uuid);
        if (proposal == null)
            return ResponseEntity.notFound().build();
        SendToSignatureResponseDTO response = proposalServiceBO.sendToSignature(proposal);
        return ResponseEntity.ok(response);
    }

}