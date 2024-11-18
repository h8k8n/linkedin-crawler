package com.obss.softwarecrafter.model.contract.crawling;

import com.obss.softwarecrafter.model.contract.base.BaseRequest;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class GetStatusRequest extends BaseRequest implements Serializable {
    private List<StartStopRequestV2> idList;
}
