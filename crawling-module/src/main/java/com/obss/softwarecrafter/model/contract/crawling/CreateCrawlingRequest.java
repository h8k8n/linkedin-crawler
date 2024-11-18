package com.obss.softwarecrafter.model.contract.crawling;

import com.obss.softwarecrafter.model.contract.base.BaseRequest;
import com.obss.softwarecrafter.model.enums.CrawlingTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CreateCrawlingRequest extends BaseRequest {
    private String description;
    private CrawlingTypeEnum type;
    private String target;
    private int threadCount;
    private boolean recursiveEnable;
    private int recursiveDepth;
    private Map<String, String[]> crawlingFilters;
}
