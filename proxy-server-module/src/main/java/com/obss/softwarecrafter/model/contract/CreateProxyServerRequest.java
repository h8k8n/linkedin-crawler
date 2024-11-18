package com.obss.softwarecrafter.model.contract;

import com.obss.softwarecrafter.model.dto.CreateUpdateProxyServerDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateProxyServerRequest extends BaseRequest {

    private List<CreateUpdateProxyServerDto> proxyServerList;
}
