package com.obss.softwarecrafter.model.jpa.repository;

import com.obss.softwarecrafter.model.enums.AccountStatusEnum;
import com.obss.softwarecrafter.model.jpa.entity.LinkedinAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LinkedinAccountRepository extends JpaRepository<LinkedinAccountEntity, UUID> {
    List<LinkedinAccountEntity> findByStatus(AccountStatusEnum statusEnum);
}
