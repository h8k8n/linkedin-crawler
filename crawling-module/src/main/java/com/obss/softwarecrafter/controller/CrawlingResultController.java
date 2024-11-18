package com.obss.softwarecrafter.controller;

import com.obss.softwarecrafter.model.contract.base.PageResponse;
import com.obss.softwarecrafter.model.dto.ActionsDto;
import com.obss.softwarecrafter.model.dto.crawling.CrawlingResultDto;
import com.obss.softwarecrafter.model.enums.CrawlingJobStatus;
import com.obss.softwarecrafter.model.jpa.entity.CrawlingResultEntity;
import com.obss.softwarecrafter.model.jpa.repository.CrawlingResultRepository;
import com.obss.softwarecrafter.model.jpa.repository.CrawlingResultSpecification;
import com.obss.softwarecrafter.service.SingleOperationService;
import com.obss.softwarecrafter.service.crud.CrawlingResultCrudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/crawling/result")
@RequiredArgsConstructor
public class CrawlingResultController {
    public static final String LINKEDIN_URL = "https://www.linkedin.com/in/";
    private final CrawlingResultCrudService crawlingResultCrudService;
    private final CrawlingResultRepository crawlingResultRepository;
    private final SingleOperationService singleOperationService;

    @GetMapping("/")
    @Operation(summary = "Verilen parametrelere göre ham verilerde arama yapıp sonuçları dönen servistir")
    @Parameter(name = "page", description = "Hangi sayfanın verisini döneceğini belirler, değer verilmezse, ilk sayfa döner")
    @Parameter(name = "size", description = "Bir sayfada kaç veri olacağını belirler, varsayılan değer 10")
    @Parameter(name = "sort", description = "Dönen sonucun hangi alana göre sıralanacağını belirler")
    @Parameter(name = "fullName", description = "Array içindeki isimleri içeren verileri, 'VEYA'layarak döner")
    @Parameter(name = "status", description = "Ham verinin işlenme durumuna göre veriyi filtreler")
    @Parameter(name = "crawlDateBegin", description = "En son yapılan crawl işlemi tarihine göre verilen tarihten büyük olan verileri döndürür")
    @Parameter(name = "crawlDateEnd", description = "En son yapılan crawl işlemi tarihine göre verilen tarihten küçük olan verileri döndürür")
    @Parameter(name = "processDateBegin", description = "En son yapılan ham veriden profil analiz tarihine göre verilen tarihten büyük olan verileri döndürür")
    @Parameter(name = "processDateEnd", description = "En son yapılan ham veriden profil analiz tarihine göre verilen tarihten küçük olan verileri döndürür")
    public ResponseEntity<PageResponse<CrawlingResultDto>> getAll(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size,
                                                                  @RequestParam(defaultValue = "id") String sort,
                                                                  @RequestParam(required = false) String[] fullName,
                                                                  @RequestParam(required = false) Integer status,
                                                                  @RequestParam(required = false) String crawlDateBegin,
                                                                  @RequestParam(required = false) String crawlDateEnd,
                                                                  @RequestParam(required = false) String processDateBegin,
                                                                  @RequestParam(required = false) String processDateEnd
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));

        Specification<CrawlingResultEntity> crawlingResultEntitySpecification = CrawlingResultSpecification
                .withFilters(fullName, status, crawlDateBegin, crawlDateEnd, processDateBegin, processDateEnd);
        Page<CrawlingResultEntity> itemPage = crawlingResultRepository.findAll(crawlingResultEntitySpecification, pageable);

        List<CrawlingResultDto> crawlingResultDtoList = itemPage.getContent().stream().map(crawlingResultEntity -> {
                    boolean crawlJobActive = singleOperationService.status(crawlingResultEntity.getProfileId()) == CrawlingJobStatus.RUNNING;
                    return CrawlingResultDto.builder()
                            .id(crawlingResultEntity.getId())
                            .fullName(crawlingResultEntity.getFullName())
                            .profileId(crawlingResultEntity.getProfileId())
                            .linkedinUrl(LINKEDIN_URL + crawlingResultEntity.getProfileId())
                            .lastCrawlDate(crawlingResultEntity.getCrawlDate())
                            .processStatus(Objects.isNull(crawlingResultEntity.getProcessDate()) ? 0 : 1)
                            .processDate(crawlingResultEntity.getProcessDate())
                            .actions(new ActionsDto(crawlJobActive, false))
                            .build();
                }
        ).toList();
        PageResponse<CrawlingResultDto> response = new PageResponse<>(
                crawlingResultDtoList,
                itemPage.getNumber(),
                itemPage.getSize(),
                itemPage.getTotalElements(),
                itemPage.getTotalPages(),
                itemPage.isFirst(),
                itemPage.isLast()
        );


        return ResponseEntity.ok(response);
    }
    @Operation(summary = "Verilen id değerine sahip ham veriye ait bilgileri döner")
    @GetMapping("/{id}")
    public ResponseEntity<CrawlingResultDto> getById(@PathVariable("id") UUID id) {
        CrawlingResultEntity crawlingResultEntity = crawlingResultCrudService.getById(id);
        CrawlingResultDto crawlingResultDto = CrawlingResultDto.builder()
                .id(crawlingResultEntity.getId())
                .fullName(crawlingResultEntity.getFullName())
                .profileId(crawlingResultEntity.getProfileId())
                .linkedinUrl(LINKEDIN_URL + crawlingResultEntity.getProfileId())
                .lastCrawlDate(crawlingResultEntity.getCrawlDate())
                .processStatus(Objects.isNull(crawlingResultEntity.getProcessDate()) ? 0 : 1)
                .processDate(crawlingResultEntity.getProcessDate())
                .actions(new ActionsDto(true, false))
                .build();

        return ResponseEntity.ok(crawlingResultDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Verilen id değerine sahip ham veriyi siler")
    public ResponseEntity<?> delete(@PathVariable("id") UUID id) {
        crawlingResultCrudService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/batch-delete")
    @Operation(summary = "Verilen idList değerlerine sahip ham verileri siler")
    public ResponseEntity<?> deleteByList(@RequestBody List<UUID> idList) {
        crawlingResultCrudService.deleteByIdList(idList);
        return ResponseEntity.ok().build();
    }

}
