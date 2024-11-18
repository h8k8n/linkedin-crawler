package com.obss.softwarecrafter.model;

import com.obss.softwarecrafter.model.enums.ProcessStatusEnum;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public record LinkedinProfileSummaryDto(
        UUID id,
        String profileId,
        String fullName,
        LocalDateTime processDate,
        LocalDateTime crawlDate,
        ProcessStatusEnum processStatus
) implements Serializable {}
