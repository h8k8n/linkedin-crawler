package com.obss.softwarecrafter.model.contract;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}