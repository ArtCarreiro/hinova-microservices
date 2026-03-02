package com.example.crm_service.controllers;

import com.example.crm_service.controllers.dto.SendToSignatureResponseDTO;
import com.example.crm_service.domain.Proposal;
import com.example.crm_service.repositories.ProposalRepository;
import com.example.crm_service.services.interfaces.ProposalServiceBO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/proposals")
public class ProposalController {

    @Autowired
    private ProposalServiceBO proposalServiceBO;

    @Autowired
    private ProposalRepository proposalRepository;

    @PostMapping
    public ResponseEntity<Proposal> createProposal(@Valid @RequestBody Proposal newProposal) {
        if (proposalRepository.findByUuid(newProposal.getUuid()) != null)
            throw  new ResponseStatusException(HttpStatus.CONFLICT, "Proposal already exists");
        Proposal proposal = proposalServiceBO.createProposal(newProposal);
        return proposal != null ? ResponseEntity.ok(proposal) : ResponseEntity.badRequest().build();
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Proposal> getProposal(@PathVariable String uuid) {
        return proposalRepository.findAll().stream()
                .filter(proposal -> uuid.equals(proposal.getUuid()))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{uuid}/send-to-signature")
    public ResponseEntity<SendToSignatureResponseDTO> sendToSignature(@PathVariable String uuid) {
        Proposal proposal = proposalRepository.findByUuid(uuid);
        if (proposal == null)
            return ResponseEntity.notFound().build();
        SendToSignatureResponseDTO response = proposalServiceBO.sendToSignature(proposal);
        return ResponseEntity.ok(response);
    }

}