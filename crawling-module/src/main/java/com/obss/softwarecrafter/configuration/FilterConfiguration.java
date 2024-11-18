package com.obss.softwarecrafter.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(
        prefix = "crawler"
)
public class FilterConfiguration {
    private List<FilterDefinition> filterDefinitions;

    @Data
    public static class FilterDefinition {
        private String key;
        private String label;
        private boolean active;
        private String[] values;
    }
}
