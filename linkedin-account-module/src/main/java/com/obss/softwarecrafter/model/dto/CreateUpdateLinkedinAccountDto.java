package com.obss.softwarecrafter.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUpdateLinkedinAccountDto {
    private String username;
    private String password;
    private int status;
}
