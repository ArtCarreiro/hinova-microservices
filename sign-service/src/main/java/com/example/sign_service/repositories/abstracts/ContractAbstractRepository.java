package com.example.sign_service.repositories.abstracts;

import com.example.sign_service.domains.Contract;
import com.example.sign_service.repositories.UuidRepository;

public interface ContractAbstractRepository<U extends Contract> extends UuidRepository<U, String>  {}