package com.habittrigger.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Normalizes DB_URL and DATABASE_URL environment variables at the earliest Spring Boot initialization phase.
 * Converts postgres:// or postgresql:// (and handles embedded username/password) to standard JDBC format:
 * jdbc:postgresql://host:port/dbname
 */
public class DatabaseConfig implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String rawUrl = environment.getProperty("DB_URL");
        if (rawUrl == null || rawUrl.trim().isEmpty()) {
            rawUrl = environment.getProperty("DATABASE_URL");
        }

        if (rawUrl != null && !rawUrl.trim().isEmpty()) {
            rawUrl = rawUrl.trim();
            Map<String, Object> map = new HashMap<>();

            try {
                if (rawUrl.startsWith("postgres://") || rawUrl.startsWith("postgresql://")) {
                    String httpUrl = rawUrl.replaceFirst("^postgres(ql)?://", "http://");
                    URI uri = new URI(httpUrl);

                    String host = uri.getHost();
                    int port = uri.getPort();
                    String path = uri.getPath();
                    String query = uri.getQuery();
                    String userInfo = uri.getUserInfo();

                    StringBuilder jdbcUrl = new StringBuilder("jdbc:postgresql://").append(host);
                    if (port != -1) {
                        jdbcUrl.append(":").append(port);
                    }
                    if (path != null) {
                        jdbcUrl.append(path);
                    }
                    if (query != null && !query.isEmpty()) {
                        jdbcUrl.append("?").append(query);
                    }

                    map.put("spring.datasource.url", jdbcUrl.toString());
                    map.put("DB_URL", jdbcUrl.toString());

                    if (userInfo != null && userInfo.contains(":")) {
                        String[] parts = userInfo.split(":", 2);
                        map.put("spring.datasource.username", parts[0]);
                        map.put("spring.datasource.password", parts[1]);
                    }
                } else if (rawUrl.startsWith("jdbc:postgresql://")) {
                    String sub = rawUrl.substring("jdbc:postgresql://".length());
                    if (sub.contains("@")) {
                        String httpUrl = "http://" + sub;
                        URI uri = new URI(httpUrl);

                        String host = uri.getHost();
                        int port = uri.getPort();
                        String path = uri.getPath();
                        String query = uri.getQuery();
                        String userInfo = uri.getUserInfo();

                        StringBuilder jdbcUrl = new StringBuilder("jdbc:postgresql://").append(host);
                        if (port != -1) {
                            jdbcUrl.append(":").append(port);
                        }
                        if (path != null) {
                            jdbcUrl.append(path);
                        }
                        if (query != null && !query.isEmpty()) {
                            jdbcUrl.append("?").append(query);
                        }

                        map.put("spring.datasource.url", jdbcUrl.toString());
                        map.put("DB_URL", jdbcUrl.toString());

                        if (userInfo != null && userInfo.contains(":")) {
                            String[] parts = userInfo.split(":", 2);
                            map.put("spring.datasource.username", parts[0]);
                            map.put("spring.datasource.password", parts[1]);
                        }
                    } else {
                        map.put("spring.datasource.url", rawUrl);
                    }
                }

                if (!map.isEmpty()) {
                    environment.getPropertySources().addFirst(new MapPropertySource("fixedDbUrl", map));
                }
            } catch (Exception e) {
                String fixedUrl = rawUrl.replace("postgres://", "jdbc:postgresql://")
                                        .replace("postgresql://", "jdbc:postgresql://");
                map.put("spring.datasource.url", fixedUrl);
                environment.getPropertySources().addFirst(new MapPropertySource("fixedDbUrl", map));
            }
        }
    }
}

