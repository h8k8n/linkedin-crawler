package com.obss.softwarecrafter.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@OpenAPIDefinition
@Configuration
public class OpenApiConfigs {
  @Bean
  public OpenAPI userOpenAPI() {
    return new OpenAPI()
            .info(new Info()
                    .title("API Gateway")
                    .description("API Gateway Documentation")
                    .version("1.0")
                    .license(new License()
                            .name("Apache 2.0")
                            .url("http://springdoc.org")))
            .servers(List.of(new Server().url("http://localhost:8888")));
  }

  private SecurityScheme createAPIKeyScheme() {
    return new SecurityScheme().type(SecurityScheme.Type.HTTP)
            .bearerFormat("JWT")
            .scheme("bearer");
  }
}