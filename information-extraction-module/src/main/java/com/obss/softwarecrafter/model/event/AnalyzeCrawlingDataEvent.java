package com.obss.softwarecrafter.model.event;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class AnalyzeCrawlingDataEvent extends BaseEvent implements Serializable {
    private String id;
    private String profileId;
    private String rawData;
    private LocalDateTime crawlDate;
}
