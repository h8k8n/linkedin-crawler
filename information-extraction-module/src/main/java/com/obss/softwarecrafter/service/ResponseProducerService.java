package com.obss.softwarecrafter.service;

import com.obss.softwarecrafter.model.event.AnalyzeCrawlingDataEvent;
import com.obss.softwarecrafter.model.event.CompletedAnalyzingEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
public class ResponseProducerService {

    private final KafkaTemplate<String, CompletedAnalyzingEvent> kafkaTemplate;

    public void sendMessage(String id, CompletedAnalyzingEvent event) {
        kafkaTemplate.send("COMPLETED_ANALYZE_PROFILE", id, event);
    }
}
