package com.obss.softwarecrafter.model.dto.proxy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProxyServerDto {
    private String id;
    private String ip;
    private int port;
    private String username;
    private String password;
    private String type;
}
