package com.example.crm_service.repositories;

import com.example.crm_service.domain.Base;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface UuidRepository<V extends Base,K extends Serializable> extends JpaRepository<V, K> {

    V findByUuid(String uuid);

}
