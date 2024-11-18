package com.obss.softwarecrafter.service;

import com.obss.softwarecrafter.model.event.CompletedAnalyzingEvent;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ResponseHandler {
    private final Map<String, CompletableFuture<CompletedAnalyzingEvent>> futures = new ConcurrentHashMap<>();

    public CompletableFuture<CompletedAnalyzingEvent> createFuture(String id) {
        CompletableFuture<CompletedAnalyzingEvent> future = new CompletableFuture<>();
        futures.put(id, future);
        return future;
    }

    public void completeFuture(String id, CompletedAnalyzingEvent result) {
        CompletableFuture<CompletedAnalyzingEvent> future = futures.get(id);
        if (future != null) {
            future.complete(result);
        }
    }

    public void removeFuture(String id) {
        futures.remove(id);
    }
}
