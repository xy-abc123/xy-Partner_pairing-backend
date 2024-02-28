package com.xy.project.config;

import io.lettuce.core.RedisClient;
import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * redisson配置
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedissonConfig {

    private String host;

    private String port;

    private String password;

    @Bean
    public RedissonClient redisClient(){
        //1.创建配置
        Config config = new Config();
        String redisAddress=String.format("redis://%s:%s",host,port);
        config.useSingleServer()
                .setAddress(redisAddress)
                .setDatabase(0)
                .setPassword("111111");

        RedissonClient redisson=Redisson.create(config);
        return redisson;
    }
}
