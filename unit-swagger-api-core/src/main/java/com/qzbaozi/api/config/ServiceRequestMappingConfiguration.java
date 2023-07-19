package com.qzbaozi.api.config;

import com.qzbaozi.api.convert.ServiceHandlerMapping;
import com.qzbaozi.api.convert.ServiceMethodArgumentResolver;
import com.qzbaozi.api.convert.ServiceMethodReturnValueHandler;
import com.qzbaozi.api.swagger.selector.DefaultServiceSelector;
import com.qzbaozi.api.swagger.selector.ServiceMethodSelector;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sufh
 */
@Configuration
@EnableConfigurationProperties(ApiProperties.class)
public class ServiceRequestMappingConfiguration {

    @Resource
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @Bean
    @ConditionalOnMissingBean(ServiceMethodSelector.class)
    public ServiceMethodSelector serviceMethodSelector(ApiProperties apiProperties) {
        return new DefaultServiceSelector(apiProperties);
    }

    @Bean
    public ServiceHandlerMapping serviceHandlerMapping(ApiProperties apiProperties, ServiceMethodSelector serviceMethodSelector) {
        return new ServiceHandlerMapping(apiProperties, serviceMethodSelector);
    }

    @Bean
    public ServiceMethodArgumentResolver serviceMethodArgumentResolver() {
        ServiceMethodArgumentResolver serviceMethodArgumentResolver = new ServiceMethodArgumentResolver();

        // 将此参数解析器的优先级提到最高
        List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>();
        argumentResolvers.add(serviceMethodArgumentResolver);
        argumentResolvers.addAll(requestMappingHandlerAdapter.getArgumentResolvers());
        requestMappingHandlerAdapter.setArgumentResolvers(argumentResolvers);
        return serviceMethodArgumentResolver;
    }

    @Bean
    public ServiceMethodReturnValueHandler serviceMethodReturnValueHandler() {
        ServiceMethodReturnValueHandler serviceMethodReturnValueHandler = new ServiceMethodReturnValueHandler();

        // 将此结果包装器的优先级提到最高
        List<HandlerMethodReturnValueHandler> returnValueHandlers = new ArrayList<>();
        returnValueHandlers.add(serviceMethodReturnValueHandler);
        returnValueHandlers.addAll(requestMappingHandlerAdapter.getReturnValueHandlers());
        requestMappingHandlerAdapter.setReturnValueHandlers(returnValueHandlers);
        return serviceMethodReturnValueHandler;
    }

}
