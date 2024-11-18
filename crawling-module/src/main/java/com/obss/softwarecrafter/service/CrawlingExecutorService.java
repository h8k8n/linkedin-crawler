package com.obss.softwarecrafter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.obss.softwarecrafter.model.dto.crawling.CrawlingTaskDto;
import com.obss.softwarecrafter.model.dto.result.ServiceResult;
import com.obss.softwarecrafter.model.enums.CrawlingJobStatus;
import com.obss.softwarecrafter.model.jpa.entity.CrawlingMasterEntity;
import com.obss.softwarecrafter.service.crawling.CrawlingService;
import com.obss.softwarecrafter.service.crud.CrawlingCrudService;
import com.obss.softwarecrafter.utilities.CrawlingTaskUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
public class CrawlingExecutorService {
    private static final Logger logger = LoggerFactory.getLogger(CrawlingExecutorService.class);

    @Qualifier("threadPoolTaskExecutor")
    private final TaskExecutor taskExecutor;
    private final CrawlingCrudService crawlingCrudService;
    private final CrawlingService crawlingService;

    public void start(UUID id) {
        CrawlingTaskUtil.initTask(id);

        CrawlingTaskUtil.supplyAsync(taskExecutor, id, () ->
                preparationForCrawl(id)
        ).thenAccept(crawlingTaskDto -> {
                    if (crawlingTaskDto.profileUrlList().isEmpty()) {
                        completedCrawling(id);
                    } else {
                        List<CompletableFuture<Void>> taskList = crawlingTaskDto.profileUrlList()
                                .stream()
                                .map(crawlingResultData -> CrawlingTaskUtil
                                        .runAsync(taskExecutor, id, () -> crawlingService
                                                .crawlProfiles(crawlingResultData, crawlingTaskDto.recursiveDepth())))
                                .toList();
                        CrawlingTaskUtil.allOfCompletedThenRun(
                                id,
                                taskList.toArray(new CompletableFuture[0]),
                                () -> completedCrawling(id));
                    }
                }
        );
    }

    private CrawlingTaskDto preparationForCrawl(UUID id) {
        CrawlingMasterEntity crawlingMasterEntity = crawlingCrudService.getById(id);
        crawlingMasterEntity.setStatus(CrawlingJobStatus.RUNNING);
        crawlingCrudService.update(crawlingMasterEntity);
        int recursiveDepth = crawlingMasterEntity.isRecursiveEnable() ? crawlingMasterEntity.getRecursiveDepth() : 0;

        // convert JSON string to Map
        // uncheck assignment
        Map<String, String[]> filterMap = new HashMap<>();
        if (StringUtils.isNotEmpty(crawlingMasterEntity.getFilters())) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                filterMap = mapper.readValue(crawlingMasterEntity.getFilters(), new TypeReference<>() {
                });
            } catch (JsonProcessingException e) {
                logger.error("filtreleri çekerken hata oluştu", e);
            }
        }

        ServiceResult<List<String>> profileUrlListResult = crawlingService.findProfileUrlList(crawlingMasterEntity.getType(),
                crawlingMasterEntity.getTarget(), filterMap);
        if (profileUrlListResult.hasError()) {
            logger.error("profil linkleri çekilirken hata oluştu", profileUrlListResult.getException());
            return new CrawlingTaskDto(new ArrayList<>(), recursiveDepth);
        } else {
            return new CrawlingTaskDto(CrawlingTaskUtil.splitList(profileUrlListResult.getValue(), crawlingMasterEntity.getThreadCount()), recursiveDepth);
        }
    }

    private void completedCrawling(UUID id) {
        CrawlingMasterEntity crawlingMasterEntityUpdated = crawlingCrudService.getById(id);
        crawlingMasterEntityUpdated.setStatus(CrawlingJobStatus.COMPLETED);
        crawlingCrudService.update(crawlingMasterEntityUpdated);
        CrawlingTaskUtil.taskDone(id);
    }


    public CrawlingJobStatus getStatus(UUID id) {
        return crawlingCrudService.getById(id).getStatus();
    }

    public void stop(UUID id) {
        CrawlingTaskUtil.stopTask(id);
        CrawlingMasterEntity crawlingMasterEntity = crawlingCrudService.getById(id);
        crawlingMasterEntity.setStatus(CrawlingJobStatus.TERMINATED);
        crawlingCrudService.update(crawlingMasterEntity);
    }

    public boolean controlBeforeRun() {
        try {
            crawlingService.controlBeforeRun();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
