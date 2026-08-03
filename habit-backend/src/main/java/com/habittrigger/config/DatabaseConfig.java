package com.habittrigger.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Automatically normalizes database connection URLs.
 * Converts postgres:// or postgresql:// to jdbc:postgresql:// if pasted directly from cloud dashboards like Render.
 */
@Configuration
public class DatabaseConfig {

    @Bean
    @Primary
    public DataSourceProperties dataSourceProperties(DataSourceProperties properties) {
        String url = properties.getUrl();
        if (url != null) {
            if (url.startsWith("postgres://")) {
                properties.setUrl(url.replace("postgres://", "jdbc:postgresql://"));
            } else if (url.startsWith("postgresql://")) {
                properties.setUrl(url.replace("postgresql://", "jdbc:postgresql://"));
            }
        }
        return properties;
    }
}
