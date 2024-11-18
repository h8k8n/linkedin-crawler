package com.obss.softwarecrafter.exception;


import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class CrawlerDataNotFoundException extends RuntimeException{
    private final UUID id;
    private final String entityName;


}
