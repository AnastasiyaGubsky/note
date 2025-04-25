package com.gubsky.Note.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class RedisDebugConfig {

    private final LettuceConnectionFactory factory;

    @Autowired
    private Environment env;

    public RedisDebugConfig(LettuceConnectionFactory factory) {
        this.factory = factory;
    }

    @PostConstruct
    public void dump() {
        System.out.printf(">>> [REDIS DEBUG] LettuceConnectionFactory host=%s, port=%d%n",
                factory.getHostName(), factory.getPort());

        System.out.printf(">>> [REDIS DEBUG] Environment props: spring.redis.host=%s, spring.redis.port=%s, spring.redis.url=%s%n",
                env.getProperty("spring.redis.host"),
                env.getProperty("spring.redis.port"),
                env.getProperty("spring.redis.url"));
    }
}