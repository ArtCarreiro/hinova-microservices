package com.example.sign_service.controllers;

import com.example.sign_service.controllers.dto.ContractRequestDTO;
import com.example.sign_service.controllers.dto.ContractResponseDTO;
import com.example.sign_service.entities.Contract;
import com.example.sign_service.repositories.ContractRepository;
import com.example.sign_service.services.interfaces.ContractServiceBO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contracts")
public class ContractController {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private ContractServiceBO contractServiceBO;

    @PostMapping
    public ResponseEntity<ContractResponseDTO> createContract(@Valid @RequestBody ContractRequestDTO newContract) {
        ContractResponseDTO contract = contractServiceBO.createContract(newContract);
        return contract != null ? ResponseEntity.ok(contract) : ResponseEntity.badRequest().build();
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Contract> getContract(@PathVariable String uuid) {
        return contractRepository.findAll().stream()
                .filter(contract -> uuid.equals(contract.getUuid()))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{uuid}/sign")
    public ResponseEntity<Contract> signContract(@PathVariable String uuid) {
        Contract contract = contractRepository.findByUuid(uuid);
        if (contract != null)
            contractServiceBO.signContract(contract);
        return contract == null ? ResponseEntity.notFound().build() : ResponseEntity.ok().build();
    }

}