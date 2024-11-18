package com.obss.softwarecrafter.model.jpa.repository;

import com.obss.softwarecrafter.model.LinkedinProfileSummaryDto;
import com.obss.softwarecrafter.model.jpa.entity.LinkedinProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LinkedinProfileRepository extends JpaRepository<LinkedinProfile, UUID>, JpaSpecificationExecutor<LinkedinProfile> {
    Optional<LinkedinProfile> findByProfileId(String profileId);
}
