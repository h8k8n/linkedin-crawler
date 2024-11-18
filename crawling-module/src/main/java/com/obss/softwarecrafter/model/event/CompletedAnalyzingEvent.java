package com.obss.softwarecrafter.model.event;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class CompletedAnalyzingEvent extends BaseEvent implements Serializable {
    private boolean success;
    private LocalDateTime processDate;
}
