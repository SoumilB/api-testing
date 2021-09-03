package com.justanalytics.publishapi.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class HazelcastCacheConfig {

    Integer timeToLiveSeconds = 24*60*60;
    
    @Bean
    Config config() {
        Config config = new Config();

        MapConfig schemasMapConfig = new MapConfig();
        schemasMapConfig.setTimeToLiveSeconds(timeToLiveSeconds);
        config.getMapConfigs().put("schemas", schemasMapConfig);

        MapConfig tablesMapConfig = new MapConfig();
        tablesMapConfig.setTimeToLiveSeconds(timeToLiveSeconds);
        config.getMapConfigs().put("tables", tablesMapConfig);

        return config;
    }
    
}
