package com.obss.softwarecrafter.controller;

import com.obss.softwarecrafter.model.contract.crawling.*;
import com.obss.softwarecrafter.model.dto.CrawlingDto;
import com.obss.softwarecrafter.model.enums.CrawlingJobStatus;
import com.obss.softwarecrafter.model.jpa.entity.CrawlingMasterEntity;
import com.obss.softwarecrafter.model.mapper.CrawlingMapper;
import com.obss.softwarecrafter.service.CrawlingExecutorService;
import com.obss.softwarecrafter.service.crawling.CrawlingService;
import com.obss.softwarecrafter.service.crud.CrawlingCrudService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/crawling")
public class CrawlingRestController {
    private final CrawlingMapper crawlingMapper;
    private final CrawlingCrudService crawlingCrudService;
    private final CrawlingExecutorService crawlingExecutorService;
    private final CrawlingService crawlingService;

    @PostMapping("/")
    @Operation(summary = "Crawling işlemi oluşturur")
    public ResponseEntity<CreateCrawlingResponse> create(@RequestBody CreateCrawlingRequest request) {
        CrawlingMasterEntity crawlingMasterEntity = crawlingCrudService.create(crawlingMapper.createDtoToEntity(request));

        return ResponseEntity.ok(new CreateCrawlingResponse(crawlingMapper.entityToDto(crawlingMasterEntity)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Id değerine sahip crawling işlemi döner")
    public ResponseEntity<CrawlingDto> getById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(crawlingMapper.entityToDto(crawlingCrudService.getById(id)));
    }

    @GetMapping("/")
    @Operation(summary = "Tanımlı tüm crawling işlemlerini döner")
    public ResponseEntity<List<CrawlingDto>> getAll() {
        return ResponseEntity.ok(crawlingCrudService.getAll().stream().map(crawlingMapper::entityToDto).toList());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Verilen crawlingId değerine sahip crawling işlemini siler")
    public ResponseEntity<?> remove(@PathVariable("id") UUID crawlingId) {
        crawlingCrudService.deleteById(crawlingId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/batch-delete")
    @Operation(summary = "Verilen idList değerine göre crawling işlemlerini siler")
    public ResponseEntity<?> deleteByList(@RequestBody List<UUID> idList) {
        crawlingCrudService.deleteByIdList(idList);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Verilen crawlingId değerine sahip crawling işlemini günceller")
    public ResponseEntity<UpdateCrawlingResponse> update(@PathVariable UUID id, @RequestBody UpdateCrawlingRequest request ) {
        CrawlingMasterEntity updated = crawlingCrudService.update(id, crawlingMapper.updateDtoToEntity(request));
        return ResponseEntity.ok(new UpdateCrawlingResponse(crawlingMapper.entityToDto(updated)));
    }

    @GetMapping("/start/{id}")
    @Operation(summary = "Verilen crawlingId değerine sahip crawling işlemini başlatır")
    public ResponseEntity<?> start(@PathVariable UUID id) {
        if(crawlingExecutorService.controlBeforeRun()){
            crawlingExecutorService.start(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }
    }

    @GetMapping("/stop/{id}")
    @Operation(summary = "Verilen crawlingId değerine sahip crawling işlemini durdurur")
    public ResponseEntity<?> stop(@PathVariable UUID id) {
        crawlingExecutorService.stop(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/status/{id}")
    @Operation(summary = "Verilen crawlingId değerine sahip crawling işleminin durumunu döner")
    public ResponseEntity<CrawlingJobStatus> getStatus(@PathVariable UUID id) {
        return ResponseEntity.ok(crawlingExecutorService.getStatus(id));
    }

}
