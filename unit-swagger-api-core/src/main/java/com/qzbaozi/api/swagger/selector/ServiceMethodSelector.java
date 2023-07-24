package com.qzbaozi.api.swagger.selector;

import java.lang.reflect.Method;

/**
 * 发布到swagger的方法选择器
 *
 * @author sfh
 * @date 2023/7/13 15:55
 */
public interface ServiceMethodSelector {

    /**
     * 是否处理
     *
     * @param beanType
     * @return
     */
    boolean isHandler(Class<?> beanType);

    /**
     * 支持方法
     *
     * @param method
     * @param handlerType
     * @return
     */
    boolean supportsMethodType(Method method, Class<?> handlerType);
}
