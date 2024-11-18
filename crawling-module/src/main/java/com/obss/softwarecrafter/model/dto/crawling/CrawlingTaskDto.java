package com.obss.softwarecrafter.model.dto.crawling;

import java.util.List;


public record CrawlingTaskDto(List<List<String>> profileUrlList, int recursiveDepth) { }
