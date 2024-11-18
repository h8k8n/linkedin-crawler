package com.obss.softwarecrafter.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CrawlResultDto extends BaseDto implements Serializable {
    private String profileId;
    private String rawDataResult;
}
