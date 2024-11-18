package com.obss.softwarecrafter.model.jpa.repository;

import com.obss.softwarecrafter.model.enums.CrawlingTypeEnum;
import com.obss.softwarecrafter.model.jpa.entity.CrawlingMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CrawlingMasterRepository extends JpaRepository<CrawlingMasterEntity, UUID> {
    Optional<CrawlingMasterEntity> findByTargetAndType(String target, CrawlingTypeEnum type);
}
