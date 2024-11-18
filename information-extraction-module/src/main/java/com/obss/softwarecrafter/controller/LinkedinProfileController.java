package com.obss.softwarecrafter.controller;

import com.obss.softwarecrafter.model.ActionsDto;
import com.obss.softwarecrafter.model.LinkedinProfileSummaryDto;
import com.obss.softwarecrafter.model.SummaryDto;
import com.obss.softwarecrafter.model.contract.PageResponse;
import com.obss.softwarecrafter.model.jpa.entity.LinkedinProfile;
import com.obss.softwarecrafter.model.jpa.repository.LinkedinProfileRepository;
import com.obss.softwarecrafter.model.jpa.repository.LinkedinProfileSpecification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/linkedin-profile")
@RequiredArgsConstructor
public class LinkedinProfileController {
    private final LinkedinProfileRepository linkedinProfileRepository;

    @GetMapping("/")
    @Operation(summary = "Verilen parametrelere göre ham verilerde arama yapıp sonuçları profile özet bilgilerini dönen servistir")
    @Parameter(name = "page", description = "Hangi sayfanın verisini döneceğini belirler, değer verilmezse, ilk sayfa döner")
    @Parameter(name = "size", description = "Bir sayfada kaç veri olacağını belirler, varsayılan değer 10")
    @Parameter(name = "sortField", description = "Dönen sonucun hangi alana göre sıralanacağını belirler")
    @Parameter(name = "sortOrder", description = "Büyükten küçüğe sıralamak için -1, küçükten büyüğe sıralamak için1, varsayılan değer 1")
    @Parameter(name = "fullName", description = "Array içindeki isimleri içeren verileri, 'VEYA'layarak döner")
    @Parameter(name = "crawlDateBegin", description = "En son yapılan crawl işlemi tarihine göre verilen tarihten büyük olan verileri döndürür")
    @Parameter(name = "crawlDateEnd", description = "En son yapılan crawl işlemi tarihine göre verilen tarihten küçük olan verileri döndürür")
    @Parameter(name = "processDateBegin", description = "En son yapılan ham veriden profil analiz tarihine göre verilen tarihten büyük olan verileri döndürür")
    @Parameter(name = "processDateEnd", description = "En son yapılan ham veriden profil analiz tarihine göre verilen tarihten küçük olan verileri döndürür")
    @Parameter(name = "companies", description = "Verilen parametrelere göre profillerdeki COMPANY alanında filtreleme yapar, eğer TITLE filtresi de varsa VE'leyerek sonuç döner")
    @Parameter(name = "titles", description = "Verilen parametrelere göre profillerdeki TITLE alanında filtreleme yapar, eğer COMPANY filtresi de varsa VE'leyerek sonuç döner")
    @Parameter(name = "schools", description = "Verilen parametrelere göre profillerdeki SCHOOL alanında filtreleme yapar, eğer DEPARTMENT filtresi de varsa VE'leyerek sonuç döner")
    @Parameter(name = "departments", description = "Verilen parametrelere göre profillerdeki DEPARTMENT alanında filtreleme yapar, eğer SCHOOL filtresi de varsa VE'leyerek sonuç döner")
    public ResponseEntity<PageResponse<SummaryDto>> getAll(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size,
                                                           @RequestParam(defaultValue = "id") String sortField,
                                                           @RequestParam(defaultValue = "1") String sortOrder,
                                                           @RequestParam(required = false) String[] fullName,
                                                           @RequestParam(required = false) String crawlDateBegin,
                                                           @RequestParam(required = false) String crawlDateEnd,
                                                           @RequestParam(required = false) String processDateBegin,
                                                           @RequestParam(required = false) String processDateEnd,
                                                           @RequestParam(required = false) String[] companies,
                                                           @RequestParam(required = false) String[] titles,
                                                           @RequestParam(required = false) String[] schools,
                                                           @RequestParam(required = false) String[] departments) {
        Sort.Direction direction = sortOrder.equals("-1") ? Sort.Direction.DESC : Sort.Direction.ASC;

        String entitySortField = switch (sortField) {
            case "fullName" -> "fullName";
            case "crawlDate" -> "crawlDate";
            case "processDate" -> "lastModifiedDate";
            default -> "id";
        };

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, entitySortField));

        Specification<LinkedinProfile> linkedinProfileSpecification = LinkedinProfileSpecification
                .withFilters(fullName, crawlDateBegin, crawlDateEnd, processDateBegin, processDateEnd, companies, titles, schools, departments);
        Page<LinkedinProfile> linkedinProfilePage = linkedinProfileRepository.findAll(linkedinProfileSpecification, pageable);
        List<SummaryDto> summaryDtos = linkedinProfilePage.getContent().stream().map(linkedinProfile -> new SummaryDto(
                new LinkedinProfileSummaryDto(linkedinProfile.getId(), linkedinProfile.getProfileId(), linkedinProfile.getFullName(), linkedinProfile.getLastModifiedDate(), linkedinProfile.getCrawlDate(), linkedinProfile.getStatus()),
                new ActionsDto(false, false)
        )).toList();
        PageResponse<SummaryDto> response = new PageResponse<>(
                summaryDtos,
                linkedinProfilePage.getNumber(),
                linkedinProfilePage.getSize(),
                linkedinProfilePage.getTotalElements(),
                linkedinProfilePage.getTotalPages(),
                linkedinProfilePage.isFirst(),
                linkedinProfilePage.isLast()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/detail/")
    @Operation(summary = "Verilen ID veya ProfileId değerine profil detayını dönen servistir, ID yoksa ProfileId'ye bakar")
    public ResponseEntity<LinkedinProfile> getById(@RequestParam(required = false) UUID id,
                                                   @RequestParam(required = false) String profileId) {
        if(Objects.nonNull(id)){
            return ResponseEntity.ok(linkedinProfileRepository.findById(id).orElseThrow());
        } else if(Objects.nonNull(profileId)){
            return ResponseEntity.ok(linkedinProfileRepository.findByProfileId(profileId).orElseThrow());
        } else {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).build();
        }

    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Verilen id değerine sahip profil bilgisini siler ")
    public ResponseEntity<?> remove(@PathVariable("id") UUID id) {
        linkedinProfileRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/batch-delete")
    @Operation(summary = "Verilen idList değerlerine sahip profil bilgisini siler ")
    public ResponseEntity<?> deleteByList(@RequestBody List<UUID> idList) {
        idList.forEach(linkedinProfileRepository::deleteById);
        return ResponseEntity.ok().build();
    }
}
