package com.teakter.auth.config;

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
    public OpenAPI teakterOpenAPI(){
    return new OpenAPI()
            .info(new Info()
                    .title("teakter auth API")
                    .description("documentacion de el servicio de autenticacion")
                    .version("1.0"))
            //esto agrega el boton de autenticacion a la interfas
            .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
            .components(new Components()
                    .addSecuritySchemes("BearerAuth",
                            new SecurityScheme()
                                    .name("BearerAuth")
                                    .type(SecurityScheme.Type.HTTP)
                                    .scheme("bearer")
                                    .bearerFormat("JWT")));
}

}
