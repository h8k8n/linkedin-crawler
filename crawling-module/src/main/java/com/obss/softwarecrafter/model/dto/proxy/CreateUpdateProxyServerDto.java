package com.obss.softwarecrafter.model.dto.proxy;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUpdateProxyServerDto {
    private String ip;
    private int port;
    private String username;
    private String password;
    private int status;
}
