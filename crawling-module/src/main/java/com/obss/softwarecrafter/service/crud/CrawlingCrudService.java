package com.obss.softwarecrafter.service.crud;

import com.obss.softwarecrafter.model.enums.CrawlingJobStatus;
import com.obss.softwarecrafter.model.enums.CrawlingTypeEnum;
import com.obss.softwarecrafter.model.jpa.entity.CrawlingMasterEntity;
import com.obss.softwarecrafter.model.jpa.repository.CrawlingMasterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CrawlingCrudService extends BaseCrudService<CrawlingMasterEntity, CrawlingMasterRepository>{
    private final CrawlingMasterRepository crawlingMasterRepository;

    @Override
    protected String getEntityName() {
        return CrawlingMasterEntity.class.getSimpleName();
    }

    @Override
    protected CrawlingMasterRepository getRepository() {
        return crawlingMasterRepository;
    }

    @Override
    public CrawlingMasterEntity create(CrawlingMasterEntity entity) {
        entity.setStatus(CrawlingJobStatus.INITIALIZED);
        return super.create(entity);
    }

    public CrawlingMasterEntity update(UUID id, CrawlingMasterEntity entity) {
        CrawlingMasterEntity currentEntity = getById(id);
        currentEntity.setFilters(entity.getFilters());
        currentEntity.setDescription(entity.getDescription());
        currentEntity.setTarget(entity.getTarget());
        currentEntity.setRecursiveDepth(entity.getRecursiveDepth());
        currentEntity.setRecursiveEnable(entity.isRecursiveEnable());
        currentEntity.setThreadCount(currentEntity.getThreadCount());
        currentEntity.setType(entity.getType());
        return super.update(currentEntity);
    }

    @Override
    public List<CrawlingMasterEntity> getAll() {
        return super.getAll().stream().filter(entity -> entity.getType()!=CrawlingTypeEnum.SYSTEM).toList();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void checkAndFixCrawlingStatus() {
        getAll().forEach(entity -> {
            if(CrawlingJobStatus.RUNNING == entity.getStatus()) {
                entity.setStatus(CrawlingJobStatus.TERMINATED);
            }
            getRepository().save(entity);
        });
    }

    public Optional<CrawlingMasterEntity> getByTargetAndType(String profileId, CrawlingTypeEnum type) {
        return getRepository().findByTargetAndType(profileId, type);
    }
}
