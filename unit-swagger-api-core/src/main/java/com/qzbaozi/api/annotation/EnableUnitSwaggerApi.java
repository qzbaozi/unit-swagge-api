package com.qzbaozi.api.annotation;

import com.qzbaozi.api.config.ServiceRequestMappingConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启动注解
 * @author sufh
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ServiceRequestMappingConfiguration.class)
public @interface EnableUnitSwaggerApi {
}
