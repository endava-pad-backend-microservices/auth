package com.endava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan({"com.endava.controller","com.endava.provider","com.endava.bean","com.endava.serviceimpl"})
@EnableSwagger2
public class AuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}

	@Configuration
	class RestTemplateConfig {

		@Bean
		public RestTemplate restTemplate(RestTemplateBuilder builder) {
			return builder.build();
		}
	}
	
	@Configuration
	@EnableSwagger2
	public class SwaggerConfig {
	    @Bean
	    public Docket authAPI() {
	        return new Docket(DocumentationType.SWAGGER_2)
	                .select()                 
	                .apis(RequestHandlerSelectors.basePackage("com.endava.controller"))
	                .build();
	    }
	}

}
