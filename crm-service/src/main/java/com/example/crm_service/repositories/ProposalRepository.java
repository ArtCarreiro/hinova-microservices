package com.example.crm_service.repositories;

import com.example.crm_service.domain.Proposal;
import com.example.crm_service.repositories.abstracts.ProposalAbstractRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProposalRepository extends ProposalAbstractRepository<Proposal> {

    Proposal findByContractUuid(String uuid);
    Proposal findByIdempotencyKey(String idempotencyKey);

}
