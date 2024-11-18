package com.obss.softwarecrafter.service;

import com.obss.softwarecrafter.model.enums.AccountStatusEnum;
import com.obss.softwarecrafter.model.jpa.entity.LinkedinAccountEntity;
import com.obss.softwarecrafter.model.jpa.repository.LinkedinAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class LinkedinAccountService extends BaseCrudService<LinkedinAccountEntity, LinkedinAccountRepository> {
    private final LinkedinAccountRepository linkedinAccountRepository;

    @Override
    protected String getEntityName() {
        return LinkedinAccountEntity.class.getSimpleName();
    }

    @Override
    protected LinkedinAccountRepository getRepository() {
        return linkedinAccountRepository;
    }

    public LinkedinAccountEntity update(UUID id, LinkedinAccountEntity entity) {
        LinkedinAccountEntity currentEntity = getById(id);
        currentEntity.setPassword(entity.getPassword());
        currentEntity.setUsername(entity.getUsername());
        currentEntity.setStatus(entity.getStatus());
        return super.update(currentEntity);
    }

    public List<LinkedinAccountEntity> getByStatusList(AccountStatusEnum statusEnum){
        return getRepository().findByStatus(statusEnum);
    }

}
