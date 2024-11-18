package com.obss.softwarecrafter.model.contract.proxy;

import com.obss.softwarecrafter.model.contract.base.BaseRequest;
import com.obss.softwarecrafter.model.dto.proxy.ReadProxyServerDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateProxyServerResponse extends BaseRequest {

    private List<ReadProxyServerDto> proxyServerList;
}
