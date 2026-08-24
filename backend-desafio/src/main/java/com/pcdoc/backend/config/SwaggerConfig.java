package com.pcdoc.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Controle de Produtos - Desafio PCDoc")
                        .version("v1.0.0")
                        .description("API RESTful para controle de estoque e venda de produtos.")
                        .contact(new Contact()
                                .name("João Alberto")
                                .email("jaoalbertorsc@gmail.com")));
    }
}
