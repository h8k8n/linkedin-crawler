package com.obss.softwarecrafter.model.dto.proxy;

import com.obss.softwarecrafter.model.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReadProxyServerDto extends BaseDto {
    private String ip;
    private int port;
    private String username;
    private String password;
    private int status;
}
