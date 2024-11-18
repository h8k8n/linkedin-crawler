package com.obss.softwarecrafter.model.contract.proxy;

import com.obss.softwarecrafter.model.contract.base.BaseRequest;
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
}
