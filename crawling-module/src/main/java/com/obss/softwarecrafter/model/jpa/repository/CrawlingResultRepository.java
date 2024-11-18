package com.obss.softwarecrafter.model.jpa.repository;

import com.obss.softwarecrafter.model.jpa.entity.CrawlingResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface CrawlingResultRepository extends JpaRepository<CrawlingResultEntity, UUID>, JpaSpecificationExecutor<CrawlingResultEntity> {

    Optional<CrawlingResultEntity> findByProfileId(String profileId);
}

