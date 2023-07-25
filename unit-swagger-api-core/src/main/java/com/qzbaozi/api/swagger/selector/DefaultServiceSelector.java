package com.qzbaozi.api.swagger.selector;

import com.qzbaozi.api.annotation.UnitApiMapping;
import com.qzbaozi.api.config.ApiProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author sfh
 * @date 2023/7/13 15:58
 */
@Slf4j
public class DefaultServiceSelector implements ServiceMethodSelector {

    ApiProperties apiProperties;
    Class<? extends Annotation> annotation = null;


    public DefaultServiceSelector(ApiProperties apiProperties) {
        this.apiProperties = apiProperties;
    }

    @Override
    public boolean isHandler(Class<?> beanType) {

        Class<? extends Annotation> annotation = this.getAnnotation();

        if (!AnnotatedElementUtils.hasAnnotation(beanType, annotation)) {
            return false;
        }

        for (String path : apiProperties.getPath()) {
            String name = AopUtils.isCglibProxy(beanType) ? beanType.getSuperclass().getName() : beanType.getName();

            if (path.endsWith("*")) {
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
    public boolean supportsMethodType(Method method, Class<?> handlerType) {
        if (Modifier.isStatic(method.getModifiers())) {
            return false;
        }

        if (apiProperties.isClassOnly()) {
            if (!method.getDeclaringClass().getSimpleName().equals(handlerType.getSimpleName())) {
                return false;
            }
        }

        return Modifier.isPublic(method.getModifiers()) ||
                AnnotatedElementUtils.hasAnnotation(method, UnitApiMapping.class);
    }

    /**
     * 获取配置过滤注解
     *
     * @return
     */
    private Class<? extends Annotation> getAnnotation() {
        if (annotation != null) {
            return annotation;
        }
        if (apiProperties.getAnnotationPath() != null) {
            try {
                annotation = (Class<? extends Annotation>) Class.forName(apiProperties.getAnnotationPath());
            } catch (Exception e) {
                log.error("AnnotationPath is error :{}", apiProperties.getAnnotationPath());
            }
        }
        if (annotation == null) {
            annotation = Service.class;
        }
        return annotation;
    }
}
