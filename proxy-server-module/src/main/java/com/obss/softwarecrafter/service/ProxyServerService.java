package com.obss.softwarecrafter.service;

import com.obss.softwarecrafter.model.enums.AccountStatusEnum;
import com.obss.softwarecrafter.model.jpa.entity.ProxyServerEntity;
import com.obss.softwarecrafter.model.jpa.repository.ProxyServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ProxyServerService extends BaseCrudService<ProxyServerEntity, ProxyServerRepository> {
    private final ProxyServerRepository proxyServerRepository;

    @Override
    protected String getEntityName() {
        return ProxyServerEntity.class.getSimpleName();
    }

    @Override
    protected ProxyServerRepository getRepository() {
        return proxyServerRepository;
    }

    public ProxyServerEntity update(UUID id, ProxyServerEntity entity) {
        ProxyServerEntity currentEntity = getById(id);
        currentEntity.setIp(entity.getIp());
        currentEntity.setPort(entity.getPort());
        currentEntity.setPassword(entity.getPassword());
        currentEntity.setUsername(entity.getUsername());
        currentEntity.setStatus(entity.getStatus());
        currentEntity.setType(entity.getType());
        return super.update(currentEntity);
    }

    public List<ProxyServerEntity> getByStatusList(AccountStatusEnum statusEnum){
        return getRepository().findByStatus(statusEnum);
    }
}
