package com.obss.softwarecrafter.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Delegate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(
        prefix = "crawler"
)
@Getter
@Setter
public class CrawlerConfiguration {
    private String type;
    private Integer threadCount;
    private LinkedinParamsConfiguration linkedinParams;
    private List<String> unusedSections;

    @Getter
    @Setter
    public static class LinkedinParamsConfiguration {
        private String loginUrl;
        private Map<String, String> targetPrefixUrl;
    }

}
