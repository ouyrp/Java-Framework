//package com.wusong.web.swagger;
//
//import io.swagger.annotations.ApiOperation;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//
///**
// * @author p14
// */
//@Configuration
//@ConditionalOnProperty(name = "wsframework.swagger.enable",havingValue = "true")
//public class SwaggerAutoConfiguration {
//    /**
//     * swagger
//     * @return
//     */
//    @Bean
//    public Docket apiDocket() {
//        return (new Docket(DocumentationType.OAS_30))
//                .select()
//                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
//                .paths(PathSelectors.any())
//                .build();
//    }
//}
