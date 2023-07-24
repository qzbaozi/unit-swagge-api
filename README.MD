#使用说明
##1.1.1、仅引用核心包

    <dependency>
        <groupId>io.github.qzbaozi</groupId>
        <artifactId>unit-swagger-api-core</artifactId>
        <version>1.0.2</version>
    </dependency>
##1.1.2、启动
    
    @Configuration
    @EnableUnitSwaggerApi
    public class UnitSwaggerApiAutoConfiguration {
    }

##1.2.1、引用自启动包

      <dependency>
            <groupId>io.github.qzbaozi</groupId>
            <artifactId>unit-swagger-api-spring-start</artifactId>
            <version>1.0.2</version>
      </dependency>
##2、配置说明
service扫描顺序：[DefaultServiceSelector](unit-swagger-api-core/src/main/java/com/qzbaozi/api/swagger/selector/DefaultServiceSelector.java):
path>serviceDebug>[@UnitApiMapping](unit-swagger-api-core/src/main/java/com/qzbaozi/api/annotation/UnitApiMapping.java)

application.yml

    unit-swagger-api:
        #总开关，默认开
        enabled: true
        #service生成uri前缀
        uriPrefix: xx
        #扫描所有unit-swagger-api.annotation注解，默认true
        debug: true
        #仅转换当前类实现的方法，默认true
        classOnly: true
        #扫描路径
        path:
            - com.ser.*
        #设置swagger请求头
        header:
            - xx:144


#自定义拓展
实现[ServiceMethodSelector](unit-swagger-api-core/src/main/java/com/qzbaozi/api/swagger/selector/ServiceMethodSelector.java)并注入到spring ioc。
