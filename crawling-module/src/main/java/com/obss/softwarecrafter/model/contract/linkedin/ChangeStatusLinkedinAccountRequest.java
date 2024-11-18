package com.obss.softwarecrafter.model.contract.linkedin;

import com.obss.softwarecrafter.model.contract.base.BaseRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class ChangeStatusLinkedinAccountRequest extends BaseRequest {
    private List<UUID> idList;
    private int status;

}
