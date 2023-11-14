package com.zhangz.springbootdemocloudalibabaproduct.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * http://localhost:8888/cloud-alibaba-product/doc.html
 * 
 * @author zhangz
 * @date create by in 2023.11.14
 */
@Data
@Configuration
@EnableSwagger2
@ConfigurationProperties("swagger")
public class SwaggerConfiguration {
    private boolean enable;
    private String version;

    private List<Parameter> setHeaderToken() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        tokenPar.name("token").description("token").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());
        return pars;
    }

    @Bean
    public Docket agencyApiDoc() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(enable)
                .apiInfo(apiInfo("cloud-alibaba ", "文档中可以查询及测试接口调用参数和结果", version))
                .select().apis(RequestHandlerSelectors.basePackage("com.zhangz.springbootdemocloudalibabaproduct.controller"))
                .paths(PathSelectors.any()).build()
                .groupName("cloud-alibaba-product 接口目录" + version)
                .globalOperationParameters(null);
    }
 


    private ApiInfo apiInfo(String name, String description, String version) {
        return new ApiInfoBuilder().title(name).description(description)
                .contact(new Contact("spring-boot-demo@zhangz", "", "1804919062@qq.com"))
                .version(version).build();
    }
}
