package com.qzbaozi.api.convert;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletResponse;

public class ServiceMethodReturnValueHandler implements HandlerMethodReturnValueHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return returnType.getMethod().getDeclaringClass().getDeclaredAnnotation(Service.class) != null;
    }

    /**
     * 将结果封装成json格式
     */
    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        objectMapper.writeValue(response.getWriter(), returnValue);
        mavContainer.setRequestHandled(true);
    }
}

