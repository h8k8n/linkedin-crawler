package com.obss.softwarecrafter.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ReadLinkedinAccountDto {
    private UUID id;
    private String username;
    private String password;
    private int status;
}
