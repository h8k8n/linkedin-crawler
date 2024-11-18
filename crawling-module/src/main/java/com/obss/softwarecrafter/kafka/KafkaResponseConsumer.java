package com.obss.softwarecrafter.kafka;

import com.obss.softwarecrafter.model.event.CompletedAnalyzingEvent;
import com.obss.softwarecrafter.model.jpa.entity.CrawlingResultEntity;
import com.obss.softwarecrafter.service.ResponseHandler;
import com.obss.softwarecrafter.service.crud.CrawlingResultCrudService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class KafkaResponseConsumer {

    private final ResponseHandler responseHandler;
    private final CrawlingResultCrudService crawlingResultCrudService;
    @KafkaListener(topics = "COMPLETED_ANALYZE_PROFILE", groupId = "crawling")
    public void listen(ConsumerRecord<String, CompletedAnalyzingEvent> record) {
        System.out.println("COMPLETED_ANALYZE_PROFILE -> "+record.key()+" - "+record.value());
        try {
            CrawlingResultEntity entity = crawlingResultCrudService.getById(UUID.fromString(record.key()));
            entity.setProcessDate(record.value().getProcessDate());
            crawlingResultCrudService.update(entity);
        } catch (Exception e) {
            e.printStackTrace();
            //TODO log
        }
        responseHandler.completeFuture(record.key(), record.value());
    }
}