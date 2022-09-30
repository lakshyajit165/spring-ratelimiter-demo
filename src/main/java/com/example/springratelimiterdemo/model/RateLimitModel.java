package com.example.springratelimiterdemo.model;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;

import java.time.Duration;

public enum RateLimitModel {
    DEFAULT(4),

    USER1_DEV(100),

    USER2_DEV(50),

    USER1_ORG(250),

    USER2_ORG(500);

    private int bucketCapacity;

    private RateLimitModel(int bucketCapacity) {
        this.bucketCapacity = bucketCapacity;
    }

    public Bandwidth getLimit() {
        return Bandwidth.classic(bucketCapacity, Refill.intervally(bucketCapacity, Duration.ofMinutes(1)));
    }

    public int bucketCapacity() {
        return bucketCapacity;
    }

    public static RateLimitModel resolvePlanFromApiKey(String apiKey) {
        if (apiKey == null || apiKey.isEmpty()) {
            return DEFAULT;
        } else if (apiKey.startsWith("USER1_DEV")) {
            return USER1_DEV;
        } else if (apiKey.startsWith("USER2_DEV")) {
            return USER2_DEV;
        } else if (apiKey.startsWith("USER1_ORG")) {
            return USER1_ORG;
        } else if (apiKey.startsWith("USER2_ORG")) {
            return USER2_ORG;
        }
        return DEFAULT;

    }
}
