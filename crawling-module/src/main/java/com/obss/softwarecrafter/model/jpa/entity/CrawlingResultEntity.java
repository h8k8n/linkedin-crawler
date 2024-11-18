package com.obss.softwarecrafter.model.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(schema = "CRWM", name = "CRAWLING_RESULT")
public class CrawlingResultEntity extends BaseEntity{
    private String profileId;

    @Column(columnDefinition = "TEXT")
    private String rawDataResult;

    private String fullName;

    private LocalDateTime crawlDate;

    private LocalDateTime processDate;

    private UUID profileMasterId;//async


}
