package com.qzbaozi.api.swagger.plugin;

import com.qzbaozi.api.config.ApiProperties;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spring.web.readers.operation.OperationParameterReader;

import javax.annotation.Resource;

/**
 * @author sfh
 * @date 2023/7/14 11:37
 */
public class OperationBuilderPluginAop implements MethodInterceptor {


    @Resource
    private ApiProperties apiProperties;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object[] args = invocation.getArguments();
        OperationContext context = (OperationContext) args[0];

        if (PluginUtil.startsWith(context.requestMappingPattern(), apiProperties.getUriPrefix())) {
            if (invocation.getThis() instanceof OperationParameterReader) {
                //排除表单参数
                return null;
            }
        }
        return invocation.proceed();
    }
}
