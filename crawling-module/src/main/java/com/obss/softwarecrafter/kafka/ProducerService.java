package com.obss.softwarecrafter.kafka;

import com.obss.softwarecrafter.model.event.AnalyzeCrawlingDataEvent;
import com.obss.softwarecrafter.model.event.BaseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProducerService {
    private final KafkaTemplate<String, AnalyzeCrawlingDataEvent> kafkaTemplate;

    public void sendMessage(AnalyzeCrawlingDataEvent event) {

        CompletableFuture<SendResult<String, AnalyzeCrawlingDataEvent>> future = kafkaTemplate.send("DO_ANALYZE_PROFILE", event);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("kafka sent message success");
            } else {
                log.error("kafka send message error", ex);
            }
        });
    }
}
