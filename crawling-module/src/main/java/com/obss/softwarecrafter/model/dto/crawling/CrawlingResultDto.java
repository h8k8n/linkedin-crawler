package com.obss.softwarecrafter.model.dto.crawling;

import com.obss.softwarecrafter.model.dto.ActionsDto;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class CrawlingResultDto implements Serializable {
    private UUID id;
    private String fullName;
    private String profileId;
    private String linkedinUrl;
    private LocalDateTime lastCrawlDate;
    private int processStatus;
    private LocalDateTime processDate;
    private ActionsDto actions;
}
