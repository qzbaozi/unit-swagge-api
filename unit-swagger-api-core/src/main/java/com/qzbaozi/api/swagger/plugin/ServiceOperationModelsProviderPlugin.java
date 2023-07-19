package com.qzbaozi.api.swagger.plugin;

import com.fasterxml.classmate.TypeResolver;
import com.qzbaozi.api.config.ApiProperties;
import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationModelsProviderPlugin;
import springfox.documentation.spi.service.contexts.RequestMappingContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sfh
 * @date 2023/7/13 17:41
 */
@Slf4j
public class ServiceOperationModelsProviderPlugin implements OperationModelsProviderPlugin {

    @Autowired
    ApiProperties apiProperties;
    @Autowired
    private TypeResolver typeResolver;

    static final ClassPool classPool = ClassPool.getDefault();
    public static final String BASE_PACKAGE_PREFIX = "com.qzbaozi.api.model.";
    public static final Map<String, Class<?>> classMap = new HashMap<>();

    @Override
    public void apply(RequestMappingContext context) {
        String url = (String) context.getPatternsCondition().getPatterns().stream().findFirst().get();
        if (!PluginUtil.startsWith(url, apiProperties.getUriPrefix())) {
            return;
        }
        this.createParam(context);
    }

    /**
     * 将service入参生成一个bean
     * {
     * 入参名：入参class
     * }
     *
     * @param context
     */
    private void createParam(RequestMappingContext context) {
        if (CollectionUtils.isEmpty(context.getParameters())) {
            return;
        }

        Class<?> clazz = this.createClass(context);
        if (clazz != null) {
            context.operationModelsBuilder().addInputParam(typeResolver.resolve(clazz));
        }
    }

    private Class<?> createClass(RequestMappingContext context) {
        String name = PluginUtil.getClassName(context.getName(), context.getParameters());
        String clazzName = BASE_PACKAGE_PREFIX + context.getGroupName() + "." + name;

        if (classMap.get(clazzName) != null) {
            return classMap.get(clazzName);
        }

        try {
            CtClass tmp = classPool.getCtClass(clazzName);
            if (tmp != null) {
                tmp.detach();
            }
        } catch (NotFoundException e) {
        }
        CtClass ctClass = classPool.makeClass(clazzName);

        List<ResolvedMethodParameter> parameters = context.getParameters();
        try {
            int fieldCount = 0;
            for (ResolvedMethodParameter parameter : parameters) {
                ctClass.addField(createField(parameter, ctClass));
                fieldCount++;
            }
            if (fieldCount > 0) {
                Class<?> aClass = ctClass.toClass();
                classMap.put(clazzName, aClass);
                return aClass;
            }
        } catch (Throwable e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private static CtField createField(ResolvedMethodParameter parameter, CtClass ctClass) throws NotFoundException, CannotCompileException {
        CtField field = new CtField(getFieldType(parameter.getParameterType().getErasedType()), parameter.defaultName().get(), ctClass);
        field.setModifiers(Modifier.PUBLIC);
        ConstPool constPool = ctClass.getClassFile().getConstPool();
        AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        Annotation ann = new Annotation("io.swagger.annotations.ApiModelProperty", constPool);
        ann.addMemberValue("value", new StringMemberValue(parameter.defaultName().get(), constPool));

        attr.addAnnotation(ann);
        field.getFieldInfo().addAttribute(attr);
        return field;
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }

    public static CtClass getFieldType(Class<?> propetyType) {
        CtClass fieldType = null;
        try {
            if (!propetyType.isAssignableFrom(Void.class)) {
                fieldType = classPool.get(propetyType.getName());
            } else {
                fieldType = classPool.get(String.class.getName());
            }
        } catch (NotFoundException e) {
            //抛异常
            ClassClassPath path = new ClassClassPath(propetyType);
            classPool.insertClassPath(path);
            try {
                fieldType = classPool.get(propetyType.getName());
            } catch (NotFoundException e1) {
                //can't find
            }
        }
        return fieldType;
    }
}
