package com.wetube.gateway.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

@Bean
    public OpenAPI wetubeOpenAPI(){
    return new OpenAPI()
        .info(new Info()
            .title("wetube gateway API")
            .description("documentacion de el servicio de gateway")
            .version("1.0"))
        //esto es lo que agrega el boton de autenticacion a la interfas
        .addSecurityItem(new SecurityRequirement().addList("BearerGateway"))
        .components(new Components()
            .addSecuritySchemes("BearerGateway",
                new SecurityScheme()
                    .name("BearerGateway")
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")));
}

}
