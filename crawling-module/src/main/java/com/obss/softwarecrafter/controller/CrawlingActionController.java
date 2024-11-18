package com.obss.softwarecrafter.controller;

import com.obss.softwarecrafter.kafka.ProducerService;
import com.obss.softwarecrafter.model.contract.crawling.GetStatusRequest;
import com.obss.softwarecrafter.model.contract.crawling.GetStatusResponse;
import com.obss.softwarecrafter.model.contract.crawling.StartStopRequestV2;
import com.obss.softwarecrafter.model.dto.SingleOperationStatusDto;
import com.obss.softwarecrafter.model.enums.CrawlingJobStatus;
import com.obss.softwarecrafter.model.event.AnalyzeCrawlingDataEvent;
import com.obss.softwarecrafter.model.event.CompletedAnalyzingEvent;
import com.obss.softwarecrafter.model.jpa.entity.CrawlingResultEntity;
import com.obss.softwarecrafter.service.ResponseHandler;
import com.obss.softwarecrafter.service.SingleOperationService;
import com.obss.softwarecrafter.service.crud.CrawlingResultCrudService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api/crawling/operations/")
@RequiredArgsConstructor
public class CrawlingActionController {
    private final CrawlingResultCrudService crawlingResultCrudService;
    private final ResponseHandler responseHandler;
    private final ProducerService producerService;
    private final  SingleOperationService singleOperationService;

    @PostMapping("/process")
    @Operation(summary = "Ham veriyi işlenmesi için tetikleyen servistir")
    public ResponseEntity<CompletedAnalyzingEvent> processProfile(@RequestBody StartStopRequestV2 request) {
        CrawlingResultEntity entity = crawlingResultCrudService.getByProfileId(request.getProfileId()).orElseThrow();
        CompletableFuture<CompletedAnalyzingEvent> future = responseHandler.createFuture(entity.getId().toString());
        producerService.sendMessage(AnalyzeCrawlingDataEvent.builder()
                .id(entity.getId().toString())
                .profileId(entity.getProfileId())
                .rawData(entity.getRawDataResult())
                .crawlDate(entity.getCrawlDate())
                .build());

        try {
            // Response için bekle (timeout: 30 saniye)
            CompletedAnalyzingEvent result = future.get(30, TimeUnit.SECONDS);

            return ResponseEntity.ok(result);
        } catch (TimeoutException e) {
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } finally {
            responseHandler.removeFuture(entity.getId().toString());
        }
    }

    @PostMapping("/start")
    @Operation(summary = "Verilen profileId değerine göre tekrar crawling işlemi başlatır")
    public ResponseEntity<?> start(@RequestBody StartStopRequestV2 request) {
        singleOperationService.start(request.getProfileId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/stop")
    @Operation(summary = "Verilen profileId değerine devam eden crawling işlemi varsa durdurur")
    public ResponseEntity<?> stop(@RequestBody StartStopRequestV2 request) {
        singleOperationService.stop(request.getProfileId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/status")
    @Operation(summary = "Verilen profileId değerine crawling işlemi durumunu döner")
    public ResponseEntity<CrawlingJobStatus> getStatus(@RequestBody StartStopRequestV2 request) {
        return ResponseEntity.ok(singleOperationService.status(request.getProfileId()));
    }

    @PostMapping("/status-batch")
    @Operation(summary = "Verilen profileId listesine ait crawlinglerinin durumunu döner")
    public ResponseEntity<GetStatusResponse> getStatusBatch(@RequestBody GetStatusRequest request) {
        List<SingleOperationStatusDto> singleOperationStatusDtoList = request.getIdList().stream().map(data -> {
            CrawlingJobStatus crawlingJobStatus = singleOperationService.status(data.getProfileId());
            return new SingleOperationStatusDto(data.getProfileId(), crawlingJobStatus == CrawlingJobStatus.RUNNING);
        }).toList();

        return ResponseEntity.ok(new GetStatusResponse(singleOperationStatusDtoList));
    }

}
