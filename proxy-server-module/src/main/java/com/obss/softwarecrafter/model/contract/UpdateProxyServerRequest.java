package com.obss.softwarecrafter.model.contract;

import com.obss.softwarecrafter.model.enums.ProxyType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProxyServerRequest extends BaseRequest {
    private String ip;
    private int port;
    private String username;
    private String password;
    private int status;
    private ProxyType type;
}
