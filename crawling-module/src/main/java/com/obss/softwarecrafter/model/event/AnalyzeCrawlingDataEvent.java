package com.obss.softwarecrafter.model.event;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class AnalyzeCrawlingDataEvent extends BaseEvent implements Serializable {
    private String id;
    private String profileId;
    private String rawData;
    private LocalDateTime crawlDate;
}
