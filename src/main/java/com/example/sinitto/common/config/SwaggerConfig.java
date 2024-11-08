package com.example.sinitto.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

@Configuration
public class SwaggerConfig {

    private final Environment environment;

    private static final String LOCAL_SERVER_URL = "http://localhost:8080";
    private static final String PROD_SERVER_URL = "https://sinitto.site";

    public SwaggerConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public OpenAPI openAPI() {
        String serverUrl = LOCAL_SERVER_URL;

        if (environment.acceptsProfiles(Profiles.of("prod"))) {
            serverUrl = PROD_SERVER_URL;
        }

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .info(apiInfo())
                .addServersItem(new Server().url(serverUrl))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }

    private Info apiInfo() {
        return new Info()
                .title("나만의 작은 시니또")
                .description("아자아자 화이팅")
                .version("0.0.2");
    }
}
