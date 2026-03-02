package com.example.sign_service.services.interfaces;

import com.example.sign_service.entities.Contract;

public interface ContractServiceBO {

    Contract signContract(Contract contract);

    Contract createContract(Contract contract);
}
