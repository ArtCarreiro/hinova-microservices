package com.example.sign_service.controllers;

import com.example.sign_service.entities.Contract;
import com.example.sign_service.repositories.ContractRepository;
import com.example.sign_service.services.ContractService;
import com.example.sign_service.services.interfaces.ContractServiceBO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contract")
public class ContractController {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private ContractServiceBO contractServiceBO;

    @PostMapping
    public ResponseEntity<Contract> createContract(@Valid @RequestBody Contract newContract) {
        Contract contract = contractServiceBO.createContract(newContract);
        return contract != null ? ResponseEntity.ok(contract) : ResponseEntity.badRequest().build();
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Contract> getContract(@PathVariable String uuid) {
        Contract contract = contractRepository.findByUuid(uuid);
        return contract == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(contract);
    }

    @PostMapping("/{uuid}")
    public ResponseEntity<Contract> signContract(@PathVariable String uuid) {
        Contract contract = contractRepository.findByUuid(uuid);
        if (contract != null)
            contractServiceBO.signContract(contract);
        return contract == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(contract);
    }

}