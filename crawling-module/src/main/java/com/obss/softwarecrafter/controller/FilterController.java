package com.obss.softwarecrafter.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.obss.softwarecrafter.configuration.FilterConfiguration;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/crawling")
public class FilterController {
    private final FilterConfiguration filterConfiguration;

    @GetMapping("/filter-definitions")
    @Operation(summary = "Uygulama ayar dosyasında filtre tanımlarını döner")
    public ResponseEntity<List<FilterConfiguration.FilterDefinition>> getFilterDefinitions() {
        return ResponseEntity.ok(filterConfiguration.getFilterDefinitions()
                .stream()
                .filter(FilterConfiguration.FilterDefinition::isActive)
                .toList());
    }
}
