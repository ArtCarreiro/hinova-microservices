package com.example.sign_service.controllers;

import com.example.sign_service.dto.ContractRequestDTO;
import com.example.sign_service.dto.ContractResponseDTO;
import com.example.sign_service.domains.Contract;
import com.example.sign_service.repositories.ContractRepository;
import com.example.sign_service.services.interfaces.ContractServiceBO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contracts")
@Tag(name = "Contracts", description = "Operações de contrato no SIGN")
public class ContractController {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private ContractServiceBO contractServiceBO;

    @PostMapping
    @Operation(summary = "Criar contrato", description = "Cria um contrato a partir do externalProposalId enviado pelo CRM.")
    public ResponseEntity<ContractResponseDTO> createContract(@Valid @RequestBody ContractRequestDTO newContract) {
        ContractResponseDTO contract = contractServiceBO.createContract(newContract);
        return contract != null ? ResponseEntity.ok(contract) : ResponseEntity.badRequest().build();
    }

    @GetMapping("/{uuid}")
    @Operation(summary = "Consultar contrato", description = "Retorna os dados de um contrato pelo UUID.")
    public ResponseEntity<Contract> getContract(@PathVariable String uuid) {
        Contract contract = contractRepository.findByUuid(uuid);
        return contract != null ? ResponseEntity.ok(contract) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{uuid}/sign")
    @Operation(summary = "Assinar contrato", description = "Marca o contrato como assinado e notifica o CRM via callback.")
    public ResponseEntity<Contract> signContract(@PathVariable String uuid) {
        Contract contract = contractRepository.findByUuid(uuid);
        if (contract != null)
            contractServiceBO.signContract(contract);
        return contract == null ? ResponseEntity.notFound().build() : ResponseEntity.ok().build();
    }

}
