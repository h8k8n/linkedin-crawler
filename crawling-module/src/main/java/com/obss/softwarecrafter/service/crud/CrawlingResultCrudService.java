package com.obss.softwarecrafter.service.crud;

import com.obss.softwarecrafter.model.jpa.entity.CrawlingResultEntity;
import com.obss.softwarecrafter.model.jpa.repository.CrawlingResultRepository;
import com.obss.softwarecrafter.utilities.HtmlUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CrawlingResultCrudService extends BaseCrudService<CrawlingResultEntity, CrawlingResultRepository> {
    private final CrawlingResultRepository crawlingResultRepository;

    @Override
    protected String getEntityName() {
        return CrawlingResultEntity.class.getSimpleName();
    }

    @Override
    protected CrawlingResultRepository getRepository() {
        return crawlingResultRepository;
    }

    public void cleanRawData(UUID id) {
        CrawlingResultEntity entity = getById(id);
        CrawlingResultEntity newEntity = new CrawlingResultEntity();
        newEntity.setRawDataResult(HtmlUtils.cleanHtml(entity.getRawDataResult()));
        newEntity.setProfileId(entity.getProfileId());
        create(newEntity);
    }

    public Optional<CrawlingResultEntity> getByProfileId(String profileId) {
        return getRepository().findByProfileId(profileId);
    }
}
