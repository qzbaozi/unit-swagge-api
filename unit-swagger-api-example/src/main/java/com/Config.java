package com;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @author sfh
 * @date 2023/7/5 11:25
 */
@Configuration
@EnableSwagger2WebMvc
//@EnableUnitSwaggerApi
//@ConditionalOnProperty(name = "unit-swagger-api.enabled", havingValue = "true", matchIfMissing = true)
public class Config {

    /**
     * 支持自定义docket
     *
     * @return
     */
    @Bean
    public Docket ser() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("ser")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ser"))
                .paths(PathSelectors.any())
                .build();
    }
}
