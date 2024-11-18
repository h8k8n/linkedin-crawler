package com.obss.softwarecrafter.model.contract;

import com.obss.softwarecrafter.model.dto.ReadProxyServerDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateProxyServerResponse extends BaseRequest {

    private ReadProxyServerDto proxyServerDto;
}
