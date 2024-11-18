package com.obss.softwarecrafter.model.contract;

import com.obss.softwarecrafter.model.dto.ReadLinkedinAccountDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChangeStatusLinkedinAccountResponse extends BaseRequest {

    private List<ReadLinkedinAccountDto> linkedinAccountList;
}
