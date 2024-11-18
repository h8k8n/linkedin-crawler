package com.obss.softwarecrafter.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ProcessStatusEnum {
    RUNNING(0),
    DONE(1);

    private final int value;

    ProcessStatusEnum(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
