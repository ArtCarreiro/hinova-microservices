package com.example.crm_service.controllers;

import com.example.crm_service.dto.ContractSignedCallbackRequestDTO;
import com.example.crm_service.services.interfaces.CallbackServiceBO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sign/callbacks")
public class CallbacksController {

    @Autowired
    private CallbackServiceBO callbackServiceBO;

    @PostMapping("/contract-signed")
    public ResponseEntity<Void> contractSigned(@Valid @RequestBody ContractSignedCallbackRequestDTO request) {
        callbackServiceBO.handleContractSignedCallback(request);
        return ResponseEntity.noContent().build();
    }
}
