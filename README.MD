#maven引用
    <dependency>
        <groupId>com.qzbaozi</groupId>
        <artifactId>unit-swagger-api-spring-start</artifactId>
        <version>1.0.0</version>
    </dependency>
#配置说明
service扫描顺序：[DefaultServiceSelector](unit-swagger-api-core/src/main/java/com/qzbaozi/api/swagger/selector/DefaultServiceSelector.java):
path>serviceDebug>[@UnitApiMapping](unit-swagger-api-core/src/main/java/com/qzbaozi/api/annotation/UnitApiMapping.java)

application.yml

    unit-swagger-api:
        #总开关，默认开
        enabled: true
        #service生成uri前缀
        uriPrefix: xx
        #扫描所有service注解
        serviceDebug: true
        #扫描路径
        path:
            - com.ser.*
        #设置swagger请求头
        header:
            - xx:144


#自定义拓展
实现[ServiceMethodSelector](unit-swagger-api-core/src/main/java/com/qzbaozi/api/swagger/selector/ServiceMethodSelector.java)并注入到spring ioc。
