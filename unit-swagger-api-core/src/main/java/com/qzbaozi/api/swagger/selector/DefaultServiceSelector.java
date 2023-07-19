package com.qzbaozi.api.swagger.selector;

import com.qzbaozi.api.annotation.UnitApiMapping;
import com.qzbaozi.api.config.ApiProperties;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author sfh
 * @date 2023/7/13 15:58
 */
public class DefaultServiceSelector implements ServiceMethodSelector {

    ApiProperties apiProperties;

    public DefaultServiceSelector(ApiProperties apiProperties) {
        this.apiProperties = apiProperties;
    }

    @Override
    public boolean isHandler(Class<?> beanType) {
        if (!AnnotatedElementUtils.hasAnnotation(beanType, Service.class)) {
            return false;
        }

        for (String path : apiProperties.getPath()) {
            String name = AopUtils.isCglibProxy(beanType) ? beanType.getSuperclass().getName() : beanType.getName();

            if (path.endsWith(".*")) {
                return name.startsWith(path.replace("*", ""));
            } else {
                return name.equals(path);
            }
        }

        // 开启service debug模式@Service对象自动发布http接口
        if (apiProperties.isServiceDebug()) {
            return true;
        }

        return AnnotatedElementUtils.hasAnnotation(beanType, UnitApiMapping.class);
    }

    @Override
    public boolean supportsMethodType(Method method) {
        if (Modifier.isStatic(method.getModifiers())) {
            return false;
        }
        return Modifier.isPublic(method.getModifiers()) ||
                AnnotatedElementUtils.hasAnnotation(method, UnitApiMapping.class);
    }
}
