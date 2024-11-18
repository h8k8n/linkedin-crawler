package com.obss.softwarecrafter.model.contract.proxy;

import com.obss.softwarecrafter.model.contract.base.BaseRequest;
import com.obss.softwarecrafter.model.dto.proxy.CreateUpdateProxyServerDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateProxyServerRequest extends BaseRequest {

    private List<CreateUpdateProxyServerDto> proxyServerList;
}
