package com.qzbaozi.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认路径优先、
 *
 * @author sfh
 * @date 2023/7/5 11:38
 */
@Data
@ConfigurationProperties(prefix = ApiProperties.PROPERTY_PREFIX)
public class ApiProperties {
    public static final String PROPERTY_PREFIX = "unit-swagger-api";
    /**
     * 接口默认前缀
     */
    private String uriPrefix = "swagger-api";

    /**
     * service 扫描路径
     */
    private List<String> path = new ArrayList<>();

    /**
     * 请求头，冒号分隔   xxx:yyy
     */
    private List<String> header = new ArrayList<>();

    /**
     * 开启debug模式
     */
    private boolean serviceDebug = true;
}
