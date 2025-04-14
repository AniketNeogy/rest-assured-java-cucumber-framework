package com.petstore.api.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Getter
@Slf4j
public class ApiConfig {
    private static final String CONFIG_FILE = "src/test/resources/config/config.properties";
    private static ApiConfig instance;
    private final Properties properties;

    private final String baseUrl;
    private final int requestTimeout;
    private final boolean logAllRequests;
    private final boolean logAllResponses;

    private ApiConfig() {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            properties.load(fis);
            log.info("Loaded configuration from: {}", CONFIG_FILE);
        } catch (IOException e) {
            log.error("Failed to load configuration file: {}", CONFIG_FILE, e);
            throw new RuntimeException("Failed to load configuration", e);
        }

        baseUrl = properties.getProperty("api.base.url", "https://petstore.swagger.io/v2");
        requestTimeout = Integer.parseInt(properties.getProperty("api.request.timeout", "10000"));
        logAllRequests = Boolean.parseBoolean(properties.getProperty("api.log.all.requests", "true"));
        logAllResponses = Boolean.parseBoolean(properties.getProperty("api.log.all.responses", "true"));
    }

    public static synchronized ApiConfig getInstance() {
        if (instance == null) {
            instance = new ApiConfig();
        }
        return instance;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
} 