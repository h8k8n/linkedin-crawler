package com.obss.softwarecrafter.model.event;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class CompletedAnalyzingEvent extends BaseEvent implements Serializable {
    private boolean success;
    private LocalDateTime processDate;
}
