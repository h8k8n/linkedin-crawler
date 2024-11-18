package com.obss.softwarecrafter.model.contract;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateLinkedinAccountRequest extends BaseRequest {
    private String username;
    private String password;
    private int status;
}
