package com.ll.readycode.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  final String securitySchemeName = "Authorization";

  @Bean
  public OpenAPI customOpenAPI() {

    return new OpenAPI()
        .info(new Info()
                .title("ReadyCode API")
                .version("v1")
                .description("ReadyCode 서비스의 백엔드 API 명세서입니다.")
        )

        .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Server"))
        )

        .components(new Components()
                .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"))
        )

        .addSecurityItem(new SecurityRequirement()
                .addList(securitySchemeName)
        );
  }
}
