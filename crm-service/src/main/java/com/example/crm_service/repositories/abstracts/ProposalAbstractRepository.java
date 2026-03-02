package com.example.crm_service.repositories.abstracts;

import com.example.crm_service.entities.Proposal;
import com.example.crm_service.repositories.UuidRepository;

public interface ProposalAbstractRepository<U extends Proposal> extends UuidRepository<U, String>  {


}