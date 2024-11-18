package com.obss.softwarecrafter.model.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.obss.softwarecrafter.model.contract.crawling.UpdateCrawlingRequest;
import com.obss.softwarecrafter.model.dto.CrawlingDto;
import com.obss.softwarecrafter.model.contract.crawling.CreateCrawlingRequest;
import com.obss.softwarecrafter.model.jpa.entity.CrawlingMasterEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Mapper
public interface CrawlingMapper {
    @Mappings(
            @Mapping(source = "filters", target = "crawlingFilters", qualifiedByName = "filterStringToMap")
    )
    CrawlingDto entityToDto(CrawlingMasterEntity crawlerEntity);

    @Mappings(
            @Mapping(source = "crawlingFilters", target = "filters", qualifiedByName = "filterMapToString")
    )
    CrawlingMasterEntity createDtoToEntity(CreateCrawlingRequest createCrawlingRequest);

    @Mappings(
            @Mapping(source = "crawlingFilters", target = "filters", qualifiedByName = "filterMapToString")
    )
    CrawlingMasterEntity updateDtoToEntity(UpdateCrawlingRequest updateCrawlingRequest);

    @Named("filterMapToString")
    public static String filterMapToString(Map<String, String[]> crawlingFilters) {
        if(Objects.nonNull(crawlingFilters)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.writeValueAsString(crawlingFilters);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    @Named("filterStringToMap")
    public static Map<String, String[]> filterMapToString(String filters) {
        if(Objects.nonNull(filters)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.readValue(filters, new TypeReference<>() {
                });
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return new HashMap<>();
    }

}

