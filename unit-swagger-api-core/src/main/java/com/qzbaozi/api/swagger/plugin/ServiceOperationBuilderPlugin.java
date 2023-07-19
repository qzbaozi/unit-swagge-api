package com.qzbaozi.api.swagger.plugin;

import com.qzbaozi.api.config.ApiProperties;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sfh
 * @date 2023/7/14 10:16
 */
@Slf4j
public class ServiceOperationBuilderPlugin implements OperationBuilderPlugin {

    @Resource
    private ApiProperties apiProperties;

    @Override
    public void apply(OperationContext context) {
        context.operationBuilder().parameters(heard());

        String url = context.requestMappingPattern();
        if (!PluginUtil.startsWith(url, apiProperties.getUriPrefix())) {
            return;
        }
        List<String> paramList = context.getParameters().stream().map(item -> item.defaultName().get()).collect(Collectors.toList());
        String methodNameAlice = context.getName() + "(" + String.join(",", paramList) + ")";

        context.operationBuilder()
                .uniqueId(context.getName())
                .method(context.httpMethod())
                .position(context.operationIndex())
                .tags(Collections.singleton(context.getGroupName()))
                .notes("api:" + methodNameAlice)
                .summary(methodNameAlice);

        log.info("[{}] uri:{}", apiProperties.getUriPrefix(), context.requestMappingPattern());
        context.operationBuilder().parameters(context.getGlobalOperationParameters());
        context.operationBuilder().parameters(readParameters(context));
    }

    private List<Parameter> readParameters(final OperationContext context) {
        List<Parameter> parameters = new ArrayList<>();
        //修改Map参数的ModelRef为我们动态生成的class
        parameters.add(new ParameterBuilder()
                .parameterType("body")
                .modelRef(new ModelRef(PluginUtil.getClassName(context.getName(), context.getParameters())))
                .name("RequestBody")
                .build());
        return parameters;

    }

    public List<Parameter> heard() {
        List<Parameter> parameters = new ArrayList<>();
        for (String header : apiProperties.getHeader()) {
            String[] split = header.split(":");
            if (split.length < 2) {
                log.error("heard is err:{}", header);
                continue;
            }

            Parameter parameter = new ParameterBuilder()
                    .name(split[0])
                    .description(null)
                    .required(true)
                    .defaultValue(split[1])
                    .allowMultiple(false)
                    .modelRef(new ModelRef("string"))
                    .parameterType("header")
                    .build();
            parameters.add(parameter);
        }

        return parameters;
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }
}
