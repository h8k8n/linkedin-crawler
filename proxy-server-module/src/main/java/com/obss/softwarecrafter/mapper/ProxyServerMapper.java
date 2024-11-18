package com.obss.softwarecrafter.mapper;
import com.obss.softwarecrafter.model.contract.UpdateProxyServerRequest;
import com.obss.softwarecrafter.model.dto.CreateUpdateProxyServerDto;
import com.obss.softwarecrafter.model.dto.ReadProxyServerDto;
import com.obss.softwarecrafter.model.jpa.entity.ProxyServerEntity;
import org.mapstruct.Mapper;

@Mapper
public interface ProxyServerMapper {
    ReadProxyServerDto entityToDto(ProxyServerEntity proxyServerEntity);

    ProxyServerEntity createUpdateDtoToEntity(CreateUpdateProxyServerDto createUpdateProxyServerDto);

    ProxyServerEntity updateDtoToEntity(UpdateProxyServerRequest updateProxyServerRequest);

}

