package com.example.crm_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI crmServiceOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("CRM Service API")
                        .description("API do modulo CRM para criação de propostas, consulta e envio para assinatura.")
                        .version("v1")
                        .license(new License().name("Uso interno - Desafio Técnico")));
    }
}
