package com.obss.softwarecrafter.model.contract.crawling;

import com.obss.softwarecrafter.model.contract.base.BaseRequest;
import com.obss.softwarecrafter.model.dto.ActionsDto;
import com.obss.softwarecrafter.model.dto.SingleOperationStatusDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GetStatusResponse extends BaseRequest {
    private List<SingleOperationStatusDto> statusList;
}
