package com.obss.softwarecrafter.service;

import com.obss.softwarecrafter.model.enums.CrawlingJobStatus;
import com.obss.softwarecrafter.model.enums.CrawlingTypeEnum;
import com.obss.softwarecrafter.model.jpa.entity.CrawlingMasterEntity;
import com.obss.softwarecrafter.service.crud.CrawlingCrudService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Service
public class SingleOperationService {
    private static final Logger logger = LoggerFactory.getLogger(SingleOperationService.class);
    private static final Map<String, UUID> idMap = new ConcurrentHashMap<>();
    private final CrawlingCrudService crawlingCrudService;
    private final CrawlingExecutorService crawlingExecutorService;

    public void start(String profileId) {
        if (idMap.containsKey(profileId)) {
            //mevcut bir tanım var
            UUID id = idMap.get(profileId);
            try {
                CrawlingMasterEntity crawlingMasterEntity = crawlingCrudService.getById(id);
                if (crawlingMasterEntity.getStatus() == CrawlingJobStatus.COMPLETED || crawlingMasterEntity.getStatus() == CrawlingJobStatus.TERMINATED) {
                    crawlingExecutorService.start(crawlingMasterEntity.getId());
                    logger.info("Tanımlı olup, tamamlanmış iş tekrar başlatıldı, profileId: {}, id: {}", profileId, crawlingMasterEntity.getId());
                } else {
                    logger.warn("Devam eden iş tekrar başlatılmaya çalışıldı, profileId: {}, id: {}", profileId, crawlingMasterEntity.getId());
                }
            } catch (Exception e) {
                CrawlingMasterEntity crawlingMasterEntity = createCrawlingMasterEntityForSystem(profileId);
                crawlingExecutorService.start(crawlingMasterEntity.getId());
                idMap.put(profileId, crawlingMasterEntity.getId());
                logger.info("Yeni iş tanımlanıp başlatıldı, profileId: {}, id: {}", profileId, crawlingMasterEntity.getId());
            }
        } else {
            CrawlingMasterEntity crawlingMasterEntity = createCrawlingMasterEntityForSystem(profileId);
            crawlingExecutorService.start(crawlingMasterEntity.getId());
            idMap.put(profileId, crawlingMasterEntity.getId());
            logger.info("Yeni iş tanımlanıp başlatıldı, profileId: {}, id: {}", profileId, crawlingMasterEntity.getId());
        }
    }

    private CrawlingMasterEntity createCrawlingMasterEntityForSystem(String profileId) {
        CrawlingMasterEntity crawlingMasterEntity = new CrawlingMasterEntity();
        crawlingMasterEntity.setType(CrawlingTypeEnum.SYSTEM);
        crawlingMasterEntity.setDescription("Sistem tarafında geçici olarak oluşturulmuştur");
        crawlingMasterEntity.setTarget(profileId);
        crawlingMasterEntity.setThreadCount(1);
        crawlingMasterEntity.setRecursiveEnable(false);

        crawlingMasterEntity = crawlingCrudService.create(crawlingMasterEntity);
        return crawlingMasterEntity;
    }

    public CrawlingJobStatus status(String profileId) {
        if (idMap.containsKey(profileId)) {
            //mevcut bir tanım var
            UUID id = idMap.get(profileId);
            try {
                CrawlingJobStatus status = crawlingCrudService.getById(id).getStatus();
                if (status == CrawlingJobStatus.COMPLETED || status == CrawlingJobStatus.TERMINATED) {
                    idMap.remove(profileId);
                    crawlingCrudService.deleteById(id);
                    logger.info("İş tamamlanmış, bilgiler silindi, profileId: {}, id: {}", profileId, id);
                }
                return status;
            } catch (Exception e) {
                logger.warn("İşin durumu çekilerken beklenmeyen bir hata oluştu, profileId: {}, id: {}, hata: {}", profileId, id, ExceptionUtils.getStackTrace(e));
                return CrawlingJobStatus.COMPLETED;
            }

        } else {
            return CrawlingJobStatus.COMPLETED;
        }
    }

    public void stop(String profileId) {
        if (idMap.containsKey(profileId)) {
            UUID id = idMap.get(profileId);
            try {
                CrawlingJobStatus status = crawlingCrudService.getById(id).getStatus();
                if (status == CrawlingJobStatus.RUNNING) {
                    crawlingExecutorService.stop(id);
                }
                crawlingCrudService.deleteById(id);
                logger.info("İş durduruldu, profileId: {}, id: {}", profileId, id);
            } catch (Exception e) {
                logger.error("İş durdurulurken beklenmeyen bir hata oluştu, profileId: {}, id: {}, hata: {}", profileId, id, ExceptionUtils.getStackTrace(e));
            } finally {
                idMap.remove(profileId);
            }
        } else {
            Optional<CrawlingMasterEntity> byTargetAndType = crawlingCrudService.getByTargetAndType(profileId, CrawlingTypeEnum.SYSTEM);
            if (byTargetAndType.isPresent()) {
                CrawlingMasterEntity crawlingMasterEntity = byTargetAndType.get();
                if (crawlingMasterEntity.getStatus() == CrawlingJobStatus.RUNNING) {
                    crawlingExecutorService.stop(crawlingMasterEntity.getId());
                }
                crawlingCrudService.deleteById(crawlingMasterEntity.getId());
                logger.info("İş durduruldu, profileId: {}, id: {}", profileId, crawlingMasterEntity.getId());
            }
        }
    }
}
