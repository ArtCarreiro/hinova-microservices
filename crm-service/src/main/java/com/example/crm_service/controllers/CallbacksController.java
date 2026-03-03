package com.example.crm_service.controllers;

import com.example.crm_service.dto.ContractSignedCallbackRequestDTO;
import com.example.crm_service.services.interfaces.CallbackServiceBO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sign/callbacks")
@Tag(name = "Callbacks", description = "Endpoints de callback recebidos do SIGN")
public class CallbacksController {

    @Autowired
    private CallbackServiceBO callbackServiceBO;

    @PostMapping("/contract-signed")
    @Operation(summary = "Receber callback de contrato assinado", description = "Atualiza a proposta no CRM quando o contrato é assinado no SIGN.")
    public ResponseEntity<Void> contractSigned(@Valid @RequestBody ContractSignedCallbackRequestDTO request) {
        callbackServiceBO.handleContractSignedCallback(request);
        return ResponseEntity.noContent().build();
    }
}
