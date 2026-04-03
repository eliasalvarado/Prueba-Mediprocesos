package com.mediprocesos.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Swagger/OpenAPI para la documentación de la API.
 * Define información de la API, autenticación JWT y esquemas.
 * 
 * @author Mediprocesos
 * @version 1.0
 */
@Configuration
public class SwaggerConfig {

    /**
     * Configura la definición OpenAPI de la API.
     * 
     * @return la configuración OpenAPI
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mediprocesos Backend API")
                        .version("1.0.0")
                        .description("API REST para gestión de empleados y autenticación")
                        .contact(new Contact()
                                .name("Mediprocesos Support")
                                .url("http://mediprocesos.com")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT token for API authentication")));
    }
}
