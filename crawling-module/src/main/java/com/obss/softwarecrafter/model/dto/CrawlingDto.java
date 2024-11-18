package com.obss.softwarecrafter.model.dto;

import com.obss.softwarecrafter.model.enums.CrawlingJobStatus;
import com.obss.softwarecrafter.model.enums.CrawlingTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Setter
@Getter
public class CrawlingDto extends BaseDto implements Serializable {

    private String description;
    private CrawlingTypeEnum type;
    private String target;
    private int threadCount;
    private boolean recursiveEnable;
    private int recursiveDepth;
    private Map<String, String[]> crawlingFilters;

    private CrawlingJobStatus status;
}
