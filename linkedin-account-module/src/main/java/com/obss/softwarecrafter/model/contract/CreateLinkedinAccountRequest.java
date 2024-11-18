package com.obss.softwarecrafter.model.contract;

import com.obss.softwarecrafter.model.dto.CreateUpdateLinkedinAccountDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateLinkedinAccountRequest extends BaseRequest {

    private List<CreateUpdateLinkedinAccountDto> linkedinAccountList;
}
