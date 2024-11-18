package com.obss.softwarecrafter.model.contract.crawling;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Setter
@Getter
public class StartStopRequestV2 implements Serializable {
    private String profileId;
}
