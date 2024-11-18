package com.obss.softwarecrafter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.obss.softwarecrafter.model.enums.ProcessStatusEnum;
import com.obss.softwarecrafter.model.event.CompletedAnalyzingEvent;
import com.obss.softwarecrafter.model.jpa.entity.LinkedinProfile;
import com.obss.softwarecrafter.model.event.AnalyzeCrawlingDataEvent;
import com.obss.softwarecrafter.model.jpa.repository.LinkedinProfileRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.domain.Example;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ConsumerService {
    private final LinkedInProfileAnalyzer linkedInProfileAnalyzer;
    private final LinkedinProfileRepository linkedinProfileRepository;
    private final ResponseProducerService producerService;

    @KafkaListener(topics = "DO_ANALYZE_PROFILE", groupId = "crawling")
    public void consume(AnalyzeCrawlingDataEvent event) {
        try {
            System.out.println(event);
            LinkedinProfile linkedinProfile = linkedInProfileAnalyzer.analyzeProfile(event.getRawData());
            linkedinProfile.setProfileId(event.getProfileId());
            linkedinProfile.setStatus(ProcessStatusEnum.DONE);
            linkedinProfile.setCrawlDate(event.getCrawlDate());
            //ObjectMapper objectMapper = new ObjectMapper();

            //System.out.println(objectMapper.writeValueAsString(linkedinProfile));
            linkedinProfileRepository.findByProfileId(event.getProfileId())
                    .ifPresent(l -> linkedinProfileRepository.deleteById(l.getId()));
            LinkedinProfile saved = linkedinProfileRepository.save(linkedinProfile);
            producerService.sendMessage(event.getId(), CompletedAnalyzingEvent.builder()
                            .success(true)
                            .processDate(saved.getLastModifiedDate())
                    .build());//103 339
            //analyzeV2(event.getRawData());

        } catch (Exception e) {
            e.printStackTrace();
            //TODO
            producerService.sendMessage(event.getId(),  CompletedAnalyzingEvent.builder()
                    .success(false)
                    .build());
        }

    }
}
