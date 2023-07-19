package com.qzbaozi.api.config;

import com.qzbaozi.api.swagger.plugin.OperationBuilderPluginAop;
import com.qzbaozi.api.swagger.plugin.ServiceOperationBuilderPlugin;
import com.qzbaozi.api.swagger.plugin.ServiceOperationModelsProviderPlugin;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import springfox.documentation.spring.web.readers.operation.OperationParameterReader;

/**
 * @author sfh
 * @date 2023/7/14 11:37
 */
@Configuration
@ConditionalOnBean(OperationParameterReader.class)
public class SwaggerSupportConfiguration {
    @Bean
    public AspectJExpressionPointcutAdvisor advisor(OperationBuilderPluginAop interceptor) {
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();

        //指定代理接口,内部再区分具体类,针对性的补充处理逻辑按需处理
        advisor.setExpression("execution(* springfox.documentation.spi.service.OperationBuilderPlugin.apply(..))");
        advisor.setAdvice(interceptor);
        return advisor;
    }

    @Bean
    public ServiceOperationModelsProviderPlugin serviceOperationModelsProviderPlugin() {
        return new ServiceOperationModelsProviderPlugin();
    }

    @Bean
    public OperationBuilderPluginAop operationBuilderPluginAop() {
        return new OperationBuilderPluginAop();
    }

    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE)
    public ServiceOperationBuilderPlugin serviceOperationBuilderPlugin() {
        return new ServiceOperationBuilderPlugin();
    }

}
