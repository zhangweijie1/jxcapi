package com.mrguo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config {
    /**
     * 创建API应用
     * apiInfo() 增加API相关信息
     * 通过select()函数返回一个ApiSelectorBuilder实例,用来控制哪些接口暴露给Swagger来展现，
     * 本例采用指定扫描的包路径来定义指定要建立API的目录。
     *
     * @return
     */
    @Bean
    public Docket createBillRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("单据")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mrguo.controller"))
                .paths(PathSelectors.regex("/bill.*"))
                .build();
    }

    @Bean
    public Docket createBaseDataRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("基础资料")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mrguo.controller.basedata"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket createFinRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("财务")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mrguo.controller.fin"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket createReportRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("报表")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mrguo.controller.report"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket createLogsRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("日志")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mrguo.controller.logs"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket createSysRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("系统功能")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mrguo.controller.sys"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 创建该API的基本信息（这些基本信息会展现在文档页面中）
     * 访问地址：http://项目实际地址/swagger-ui.html
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("不二掌柜进销存API文档")
                .description("源码商业授权使用,联系微信：512830037")
                .termsOfServiceUrl("http://jxc.tao51d.com")
                .contact("mrguo")
                .version("1.0")
                .build();
    }
}