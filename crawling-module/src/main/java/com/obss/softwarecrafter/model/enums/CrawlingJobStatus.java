package com.obss.softwarecrafter.model.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.NUMBER)
public enum CrawlingJobStatus {
    INITIALIZED,
    RUNNING,
    TERMINATED,
    COMPLETED,
}
