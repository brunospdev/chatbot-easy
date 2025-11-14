package com.wpp.wppbotmanager.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("userStateCache");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterAccess(15, TimeUnit.MINUTES)
                .maximumSize(10_000)
        );

        return cacheManager;
    }
}
