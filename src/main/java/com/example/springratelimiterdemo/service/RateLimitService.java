package com.example.springratelimiterdemo.service;

import com.example.springratelimiterdemo.model.RateLimitModel;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String apiKey) {
        return cache.computeIfAbsent(apiKey, this::newBucket);
    }

    private Bucket newBucket(String apiKey) {
        RateLimitModel rateLimitModel = RateLimitModel.resolvePlanFromApiKey(apiKey);
        return bucket(rateLimitModel.getLimit());
    }

    private Bucket bucket(Bandwidth limit) {
        return Bucket4j.builder()
                .addLimit(limit)
                .build();
    }
}