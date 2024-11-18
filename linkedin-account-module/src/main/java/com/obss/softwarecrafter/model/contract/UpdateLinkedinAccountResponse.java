package com.obss.softwarecrafter.model.contract;

import com.obss.softwarecrafter.model.dto.ReadLinkedinAccountDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateLinkedinAccountResponse extends BaseRequest {

    private ReadLinkedinAccountDto linkedinAccountDto;
}
