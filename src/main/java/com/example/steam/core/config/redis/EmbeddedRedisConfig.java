package com.example.steam.core.config.redis;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

import java.io.IOException;

@Configuration
@ConditionalOnProperty(name = "embedded.redis.enabled", havingValue = "true")
public class EmbeddedRedisConfig {
    private RedisServer redisServer;
    @Value("${spring.data.redis.port}") private int redisPort;

    @PostConstruct
    void start() throws IOException {
        redisServer = new RedisServer(redisPort);
        redisServer.start();
    }

    @PreDestroy
    private void stop(){
        if (redisServer != null && redisServer.isActive()) {
            redisServer.stop();
        }
    }
}
