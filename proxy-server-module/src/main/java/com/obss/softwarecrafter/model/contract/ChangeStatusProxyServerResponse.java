package com.obss.softwarecrafter.model.contract;

import com.obss.softwarecrafter.model.dto.ReadProxyServerDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChangeStatusProxyServerResponse extends BaseRequest {

    private List<ReadProxyServerDto> proxyServerDtoList;
}
