package com.obss.softwarecrafter.model.jpa.repository;


import com.obss.softwarecrafter.model.enums.AccountStatusEnum;
import com.obss.softwarecrafter.model.jpa.entity.ProxyServerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProxyServerRepository extends JpaRepository<ProxyServerEntity, UUID> {
    List<ProxyServerEntity> findByStatus(AccountStatusEnum statusEnum);
}
