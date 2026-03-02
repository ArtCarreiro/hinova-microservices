package com.example.crm_service.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/proposals")
public class ProposalController {





}
//POST proposals (criar proposta em DRAFT)
//GET proposals/{id} (consultar)
//POST proposals/{id}/send-to-signature (chama SIGN)
//POST sign/callbacks/contract-signed (recebe notificação)