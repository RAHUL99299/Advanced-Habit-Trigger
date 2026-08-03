package com.habittrigger.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * Normalizes DB_URL environment variable at the earliest Spring Boot initialization phase.
 * Converts postgres:// or postgresql:// to jdbc:postgresql://.
 */
public class DatabaseConfig implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String dbUrl = environment.getProperty("DB_URL");
        if (dbUrl != null && !dbUrl.trim().isEmpty()) {
            String fixedUrl = dbUrl.trim();
            if (fixedUrl.startsWith("postgres://")) {
                fixedUrl = fixedUrl.replace("postgres://", "jdbc:postgresql://");
            } else if (fixedUrl.startsWith("postgresql://")) {
                fixedUrl = fixedUrl.replace("postgresql://", "jdbc:postgresql://");
            }
            if (!fixedUrl.equals(dbUrl)) {
                Map<String, Object> map = new HashMap<>();
                map.put("DB_URL", fixedUrl);
                map.put("spring.datasource.url", fixedUrl);
                environment.getPropertySources().addFirst(new MapPropertySource("fixedDbUrl", map));
            }
        }
    }
}
