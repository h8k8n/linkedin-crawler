package com.obss.softwarecrafter.model.contract.crawling;

import com.obss.softwarecrafter.model.contract.base.BaseResponse;
import com.obss.softwarecrafter.model.dto.CrawlingDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateCrawlingResponse extends BaseResponse {
    private CrawlingDto crawlingDto;
}
