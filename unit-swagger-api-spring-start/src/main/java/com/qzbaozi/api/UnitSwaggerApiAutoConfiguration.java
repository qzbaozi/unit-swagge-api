package com.qzbaozi.api;

import com.qzbaozi.api.annotation.EnableUnitSwaggerApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableUnitSwaggerApi
@ConditionalOnProperty(name = "unit-swagger-api.enabled", havingValue = "true", matchIfMissing = true)
public class UnitSwaggerApiAutoConfiguration {
}
