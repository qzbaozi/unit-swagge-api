package com.qzbaozi.api.convert;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

public class ServiceMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String SERVICE_ARGS = "service_args";
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getMethod().getDeclaringClass().getDeclaredAnnotation(Service.class) != null;
    }

    /**
     * 参数解析
     * 第一次将请求内容解析成map, 并保存到request中，下个参数解析直接从map里面取
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws IOException {
        Map<String, Object> serviceArgs = (Map<String, Object>) webRequest.getAttribute(SERVICE_ARGS, SCOPE_REQUEST);
        if (serviceArgs == null) {
            HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
            serviceArgs = objectMapper.readValue(servletRequest.getInputStream(), ModelMap.class);
            webRequest.setAttribute(SERVICE_ARGS, serviceArgs, SCOPE_REQUEST);
        }
        Object parameterValueRaw = serviceArgs.get(parameter.getParameterName());
        if (parameterValueRaw == null) {
            return null;
        }
        String parameterValueStr = objectMapper.writeValueAsString(parameterValueRaw);
        JavaType javaType = objectMapper.getTypeFactory().constructType(GenericTypeResolver.resolveType(parameter.getGenericParameterType(), parameter.getContainingClass()));
        return objectMapper.readValue(parameterValueStr, javaType);
    }
}

