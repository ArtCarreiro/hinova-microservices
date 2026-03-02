package com.example.sign_service.repositories;

import com.example.sign_service.entities.Contract;
import com.example.sign_service.repositories.abstracts.ContractAbstractRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends ContractAbstractRepository<Contract> {}
