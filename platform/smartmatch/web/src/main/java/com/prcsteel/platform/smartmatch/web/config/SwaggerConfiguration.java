package com.prcsteel.platform.smartmatch.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Created by chengui on 2016/5/23.
 */
@Configuration
@EnableWebMvc
@EnableSwagger2
@ComponentScan(basePackages ={"com.prcsteel.platform.smartmatch.web.controller"})
public class SwaggerConfiguration {
    public static final String DEFAULT_INCLUDE_PATTERN = "/api.*";

    /**
     * Swagger Springfox configuration.
     */
    @Bean
    public Docket swaggerSpringfoxDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .select()
                .paths(regex(DEFAULT_INCLUDE_PATTERN))
                .build();
    }

    /**
     * Api information
     * @return
     */
    private ApiInfo getApiInfo() {
        ApiInfo apiInfo = new ApiInfo("账户体系 API",
                "账户体系之Smartmatch模块REST API",
                "v2.0",
                "",
                "prcsteel.com",
                "The Apache License, Version 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0.html"
        );

        return apiInfo;
    }
}
