package com.qzbaozi.api.convert;

import com.qzbaozi.api.config.ApiProperties;
import com.qzbaozi.api.swagger.selector.ServiceMethodSelector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * service扫描
 */
@Slf4j
public class ServiceHandlerMapping extends RequestMappingInfoHandlerMapping {

    public static final Map<String, Integer> generated = new HashMap<>();
    private RequestMappingInfo.BuilderConfiguration config = new RequestMappingInfo.BuilderConfiguration();

    private final ApiProperties apiProperties;
    private final ServiceMethodSelector serviceMethodSelector;

    public ServiceHandlerMapping(ApiProperties apiProperties, ServiceMethodSelector serviceMethodSelector) {
        this.apiProperties = apiProperties;
        this.serviceMethodSelector = serviceMethodSelector;
    }

    /**
     * 支持@Service注解的类
     *
     * @param beanType
     * @return
     */
    @Override
    protected boolean isHandler(Class<?> beanType) {
        return serviceMethodSelector.isHandler(beanType);
    }

    /**
     * 将Service类里面的方法按照 /SimpleClassName/methodName 的方式去注册RequestMapping
     *
     * @param method
     * @param handlerType
     * @return
     */
    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        if (!serviceMethodSelector.supportsMethodType(method)) {
            return null;
        }

        String text = Arrays.stream(method.getParameters()).map(v -> v.getType().getSimpleName()).collect(Collectors.joining("-"));
        String paths = String.join("/", apiProperties.getUriPrefix(), handlerType.getSimpleName(), method.getName(), text);
        paths = this.startingWith(paths);

        RequestMappingInfo.Builder builder = RequestMappingInfo
                .paths(paths)
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .methods(RequestMethod.POST);
        return builder.options(this.config).build();
    }

    /**
     * 保证自定义的映射优先处理
     *
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }

    public String startingWith(String prefix) {
        if (generated.containsKey(prefix)) {
            generated.put(prefix, generated.get(prefix) + 1);
            String nextUniqueOperationName = String.format("%s_%s", prefix, generated.get(prefix));
            log.info("Generating unique uri: {}", nextUniqueOperationName);
            return nextUniqueOperationName;
        } else {
            generated.put(prefix, 0);
            return prefix;
        }
    }

}

